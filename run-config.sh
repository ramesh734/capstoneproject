#!/bin/sh
CRUMB=$(curl -s -u admin:admin 'http://localhost:8080/crumbIssuer/api/json' | awk -F'"' '{print $4}')
echo "Crumb: $CRUMB"
curl -s -u admin:admin 'http://localhost:8080/scriptText' --data-urlencode "script@/tmp/config.groovy" -H "Jenkins-Crumb: $CRUMB"
