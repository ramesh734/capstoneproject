#!/bin/sh
CRUMB=$(curl -s -u admin:admin 'http://localhost:8080/crumbIssuer/api/json' | sed 's/.*"crumb":"//' | sed 's/".*//')
echo "Crumb: [$CRUMB]"
RESULT=$(curl -s -u admin:admin 'http://localhost:8080/scriptText' --data-urlencode "script@/tmp/config.groovy" -H "Jenkins-Crumb: $CRUMB")
echo "Result: $RESULT"
