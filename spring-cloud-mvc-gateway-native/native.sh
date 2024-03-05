#! /bin/bash

#./mvnw clean -P native native:compile -DskipTests

# set maven and java (graalvm with native image tool in OS path variable)
mvn clean -Pnative native:compile -DskipTests && ./target/gateway

