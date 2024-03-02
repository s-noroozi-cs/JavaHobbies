#! /bin/bash

#./mvnw clean -P native native:compile -DskipTests

# set maven and java (graalvm withh native image tool in OS path variable)
mvn -Pnative spring-boot:process-aot
mvn -Pnative native:compile -DskipTests

