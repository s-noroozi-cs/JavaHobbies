#! /bin/bash

#check maven and graalvm (with native tool chain) in path of your operation correctly
mvn clean -Pnative native:compile && ./tartget/native-feign