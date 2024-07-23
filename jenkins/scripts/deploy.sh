#!/usr/bin/env sh

set -x
#docker run -d -p 80:80 --name my-apache-php-app -v c:\\Users\\Halilah\\OneDrive\\Documents\\GitHub\\7b\\src:/var/www/html php:7.2-apache
#sleep 1
docker-compose down
docker-compose up -d
set +x

echo 'Now...'
echo 'Visit http://localhost to see your PHP application in action.'