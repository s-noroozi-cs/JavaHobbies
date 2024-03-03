#! /bin/bash

mvn clean package && java -Dserver.port=0 -jar ./target/child-service-0.0.1-SNAPSHOT.jar