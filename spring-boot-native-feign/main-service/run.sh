#! /bin/bash

mvn clean package && java -Dserver.port=2020 -jar ./target/main-service-0.0.1-SNAPSHOT.jar