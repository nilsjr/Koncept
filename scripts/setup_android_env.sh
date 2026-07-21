#!/bin/bash
# Setup script for the "android-kotlin" Claude Code cloud environment.
# Configure it as the environment's setup script so every session can build
# Android projects. Requires network access to dl.google.com and
# services.gradle.org (plus its GitHub release redirect) in the network policy.
set -euo pipefail

ANDROID_HOME="${ANDROID_HOME:-$HOME/android-sdk}"
CMDLINE_TOOLS_VERSION="13114758"
# compileSdk 37 (ProjectConfig.kt); platform packages are minor-versioned now
PLATFORM_PACKAGE="platforms;android-37.0"

echo "Installing Android SDK to $ANDROID_HOME"
mkdir -p "$ANDROID_HOME/cmdline-tools"

if [ ! -x "$ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager" ]; then
  curl -fsSL -o /tmp/cmdline-tools.zip \
    "https://dl.google.com/android/repository/commandlinetools-linux-${CMDLINE_TOOLS_VERSION}_latest.zip"
  unzip -q -o /tmp/cmdline-tools.zip -d "$ANDROID_HOME/cmdline-tools"
  mv "$ANDROID_HOME/cmdline-tools/cmdline-tools" "$ANDROID_HOME/cmdline-tools/latest"
  rm /tmp/cmdline-tools.zip
fi

SDKMANAGER="$ANDROID_HOME/cmdline-tools/latest/bin/sdkmanager"
# "yes" exits 141 (SIGPIPE) when sdkmanager closes its stdin, which pipefail
# would treat as a failure - judge the pipeline by sdkmanager's status only.
set +o pipefail
yes | "$SDKMANAGER" --licenses > /dev/null
set -o pipefail
"$SDKMANAGER" --install \
  "platform-tools" \
  "$PLATFORM_PACKAGE"

# Persist environment variables for all later shells in the session.
PROFILE="$HOME/.bashrc"
if ! grep -q "ANDROID_HOME" "$PROFILE" 2>/dev/null; then
  {
    echo "export ANDROID_HOME=\"$ANDROID_HOME\""
    echo "export PATH=\"\$ANDROID_HOME/platform-tools:\$ANDROID_HOME/cmdline-tools/latest/bin:\$PATH\""
  } >> "$PROFILE"
fi

# Gradle reads the SDK location from local.properties as well.
if [ -n "${CLAUDE_PROJECT_DIR:-}" ] && [ ! -f "$CLAUDE_PROJECT_DIR/local.properties" ]; then
  echo "sdk.dir=$ANDROID_HOME" > "$CLAUDE_PROJECT_DIR/local.properties"
fi

echo "Android SDK ready: $("$SDKMANAGER" --version)"

# ---------------------------------------------------------------------------
# Pre-provision the Gradle wrapper distribution into the shared wrapper cache.
#
# Interactive cloud sessions cannot download the wrapper: services.gradle.org
# 307-redirects to github.com release assets, which the session network policy
# returns 403 for. We fetch it once here (env-build time, where the policy must
# allow the GitHub release redirect) and drop it into the exact hash-named cache
# directory `./gradlew` looks in, so every later session finds it already
# unpacked and never touches the network for the wrapper.
# ---------------------------------------------------------------------------
provision_gradle_wrapper() {
  local props="$1"
  [ -f "$props" ] || { echo "No wrapper props at $props, skipping Gradle warm"; return 0; }

  local url
  url="$(sed -n 's/^distributionUrl=//p' "$props" | tr -d '\r' | sed 's/\\:/:/g')"
  [ -n "$url" ] || { echo "No distributionUrl in $props, skipping"; return 0; }

  local zip_name base_name hash dist_dir marker
  zip_name="$(basename "$url")"                 # gradle-9.6.1-bin.zip
  base_name="${zip_name%.zip}"                  # gradle-9.6.1-bin
  # Wrapper cache dir = base36(md5(distributionUrl)); matches Gradle's
  # PathAssembler.getHash(). Passed via argv so the URL is never interpolated
  # into the Python source.
  hash="$(python3 - "$url" <<'PY' 2>/dev/null
import hashlib, sys
n = int.from_bytes(hashlib.md5(sys.argv[1].encode()).digest(), "big")
digits = "0123456789abcdefghijklmnopqrstuvwxyz"
out = ""
while n:
    n, r = divmod(n, 36)
    out = digits[r] + out
print(out or "0")
PY
)"
  if [ -z "$hash" ]; then
    echo "Could not compute wrapper hash (python3 missing?); skipping Gradle warm" >&2
    return 0
  fi

  dist_dir="${GRADLE_USER_HOME:-$HOME/.gradle}/wrapper/dists/$base_name/$hash"
  marker="$dist_dir/$zip_name.ok"

  if [ -f "$marker" ]; then
    echo "Gradle wrapper already provisioned: $dist_dir"
    return 0
  fi

  echo "Provisioning Gradle wrapper $base_name into $dist_dir"
  mkdir -p "$dist_dir"
  # Clear any aborted partial download from a prior session.
  rm -f "$dist_dir/$zip_name.part" "$dist_dir/$zip_name.lck"

  if ! curl -fsSL --connect-timeout 15 --max-time 300 -o "$dist_dir/$zip_name" "$url"; then
    echo "WARN: could not download $url" >&2
    echo "      The network policy must allow the services.gradle.org ->" >&2
    echo "      github.com release-asset redirect during env setup." >&2
    rm -f "$dist_dir/$zip_name"
    return 0
  fi

  if ! unzip -q -o "$dist_dir/$zip_name" -d "$dist_dir"; then
    echo "WARN: failed to unzip $zip_name; leaving unprovisioned for retry" >&2
    rm -f "$dist_dir/$zip_name"
    return 0
  fi
  touch "$marker"
  echo "Gradle wrapper ready: $(ls -d "$dist_dir"/*/ 2>/dev/null | head -1)"
}

provision_gradle_wrapper "${CLAUDE_PROJECT_DIR:-.}/gradle/wrapper/gradle-wrapper.properties"
