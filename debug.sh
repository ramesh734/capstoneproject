#!/bin/sh
echo "=== Raw crumb JSON ==="
curl -s -u admin:admin 'http://localhost:8080/crumbIssuer/api/json'
echo ""
echo "=== awk \$2 ==="
curl -s -u admin:admin 'http://localhost:8080/crumbIssuer/api/json' | awk -F'"' '{print $2}'
echo ""
echo "=== awk \$4 ==="
curl -s -u admin:admin 'http://localhost:8080/crumbIssuer/api/json' | awk -F'"' '{print $4}'
echo ""
echo "=== awk \$6 ==="
curl -s -u admin:admin 'http://localhost:8080/crumbIssuer/api/json' | awk -F'"' '{print $6}'
