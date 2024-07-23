#!/usr/bin/env sh

set -x
docker-compose down
set +x

echo 'Containers stopped and removed.'
