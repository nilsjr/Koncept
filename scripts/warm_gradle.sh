#!/bin/bash
# scripts/warm_gradle.sh
if [ "$CLAUDE_CODE_REMOTE" != "true" ]; then
  exit 0
fi

# Retry dependency resolution; flaky proxy connections are common in the sandbox
for i in 1 2 3; do
  ./gradlew dependencies --offline=false && break
  echo "Gradle resolve attempt $i failed, retrying..."
  sleep 5
done
exit 0