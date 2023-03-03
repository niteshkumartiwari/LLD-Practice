#!/bin/bash
APP_PORT="$1"
if [ -z "$APP_PORT" ]; then
  echo "Fatal error: Application port number must be provided to scoring script" >&2
  exit 1
fi
SCORING_DIR="$2"
if [ ! -d "$SCORING_DIR" ]; then
  echo "Fatal error: Scoring directory must be provided and must exist" >&2
  exit 1
fi
EXISTING_TESTS="$3"
if [ -z "$EXISTING_TESTS" ]; then
  echo "Fatal error: The number of existing tests at the start must be provided" >&2
  exit 1
fi
APP_BASE_URL="http://localhost:${APP_PORT}"

echo "Waiting for application to be ready on ${APP_BASE_URL}"

ready=
while [ "$ready" != "200" ]; do
  ready=$(curl -s -i "${APP_BASE_URL}/ready" | head -n1 | grep -o '200')
  sleep 1
done

echo "Application ready."

#Scoring maven test project
mvn -f "${SCORING_DIR}/pom.xml" clean test -Dserver.port="${APP_PORT}" -Dexisting.tests="${EXISTING_TESTS}"

exit 0