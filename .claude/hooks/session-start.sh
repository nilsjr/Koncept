#!/bin/bash
# SessionStart hook for Claude Code on the web (cloud sessions).
#
# Provisions everything an agent session needs to build & test this Android
# project:
#   1. Android SDK (cmdline-tools, platform-tools, platform android-37)
#   2. ANDROID_HOME/PATH exported for the session (via $CLAUDE_ENV_FILE)
#   3. local.properties with sdk.dir
#   4. dogApiKey in ~/.gradle/gradle.properties (from $DOG_API_KEY, falling
#      back to config/develop.properties)
#   5. Best-effort Gradle wrapper + dependency warm-up
#
# Network policy requirements (session environment settings):
#   - dl.google.com                      (Android SDK + Google Maven)  [required]
#   - services.gradle.org                (wrapper metadata)            [required]
#   - github.com HTTPS file downloads    (wrapper zip redirect target) [required]
#   - androidx.dev                       (compose-compiler repo)       [required]
#   - repo.maven.apache.org, plugins.gradle.org                        [required]
set -uo pipefail

if [ "${CLAUDE_CODE_REMOTE:-}" != "true" ]; then
  exit 0
fi

PROJECT_DIR="${CLAUDE_PROJECT_DIR:-$(cd "$(dirname "$0")/../.." && pwd)}"
cd "$PROJECT_DIR"

# --- 1. Android SDK (idempotent; container state is cached after the hook) ---
export ANDROID_HOME="${ANDROID_HOME:-$HOME/android-sdk}"
if ! CLAUDE_PROJECT_DIR="$PROJECT_DIR" bash scripts/setup_android_env.sh; then
  echo "session-start: Android SDK setup FAILED (is dl.google.com allowed in the network policy?)" >&2
  exit 1
fi

# --- 2. Session environment variables ---
if [ -n "${CLAUDE_ENV_FILE:-}" ]; then
  {
    echo "export ANDROID_HOME=\"$ANDROID_HOME\""
    echo "export ANDROID_SDK_ROOT=\"$ANDROID_HOME\""
    echo "export PATH=\"$ANDROID_HOME/platform-tools:$ANDROID_HOME/cmdline-tools/latest/bin:\$PATH\""
  } >> "$CLAUDE_ENV_FILE"
fi

# --- 3. local.properties (setup_android_env.sh only writes it if absent) ---
if ! grep -q "^sdk.dir=" local.properties 2>/dev/null; then
  echo "sdk.dir=$ANDROID_HOME" >> local.properties
fi

# --- 4. dogApiKey -> ~/.gradle/gradle.properties (read via findProperty) ---
mkdir -p "$HOME/.gradle"
if ! grep -q "^dogApiKey=" "$HOME/.gradle/gradle.properties" 2>/dev/null; then
  key="${DOG_API_KEY:-}"
  if [ -z "$key" ] && [ -f config/develop.properties ]; then
    key="$(grep '^dogApiKey=' config/develop.properties | cut -d= -f2-)"
  fi
  if [ -n "$key" ]; then
    echo "dogApiKey=$key" >> "$HOME/.gradle/gradle.properties"
  else
    echo "session-start: WARNING - no dogApiKey found (set DOG_API_KEY env var in the environment settings)" >&2
  fi
fi

# --- 5. Warm Gradle (best-effort: do not fail the session if the network ---
# --- policy still blocks the wrapper distribution download)              ---
if bash scripts/warm_gradle.sh; then
  echo "session-start: Gradle warmed successfully."
else
  echo "session-start: WARNING - Gradle warm-up failed. Builds will not work until" >&2
  echo "the network policy allows the Gradle 9.x wrapper download:" >&2
  echo "services.gradle.org redirects to github.com/gradle/gradle-distributions" >&2
  echo "release assets, so github.com HTTPS downloads must be allowed." >&2
fi

exit 0
