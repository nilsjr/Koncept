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