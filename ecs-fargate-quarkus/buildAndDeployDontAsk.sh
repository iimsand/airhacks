#!/bin/sh
set -e
#echo "building quarkus"
#cd quarkus && mvn clean package
echo "building CDK"
cd ./cdk && mvn clean package && cdk deploy --all --require-approval=never