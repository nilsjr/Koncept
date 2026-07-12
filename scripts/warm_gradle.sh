#!/bin/bash
# SessionStart hook: warm the Gradle wrapper + dependency cache in cloud sessions.
set -uo pipefail

if [ "${CLAUDE_CODE_REMOTE:-}" != "true" ]; then
  exit 0
fi

# Retry dependency resolution; flaky proxy connections are common in the sandbox
status=1
for i in 1 2 3; do
  if ./gradlew dependencies; then
    status=0
    break
  fi
  echo "Gradle resolve attempt $i failed, retrying..." >&2
  sleep 5
done

if [ "$status" -ne 0 ]; then
  echo "warm_gradle.sh FAILED: Gradle could not resolve dependencies." >&2
  echo "Likely causes: the wrapper distribution download was blocked by the" >&2
  echo "session network policy (services.gradle.org redirects to GitHub), or" >&2
  echo "the Android SDK is missing (run scripts/setup_android_env.sh)." >&2
fi
exit "$status"