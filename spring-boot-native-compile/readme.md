# Requirement

### Create environment

# Docker container to build

    docker build --tag=java-native-tool:java-17 -f antive-tool.docker
    
    docker run  -v cuurent-path:/app --entrypoint=/bin/bash java-native-tool:java-17

# Maven project

### Compile your code and prepared AOT using folliwng command

    mvn clean -Pnative compile spring-boot:process-aot

### Compile native using graalvm build tool maven plugin

    mvn native:compile -DskipTests

### Test native binary compiled app from target directory

    ./target/your_app_name

### Using upx tool to compress your native bunary app - without any dependency and completly portable

    upx -7 -k your_native_compiled_app

#### Options of upx command

        1. -7 option is best compression ratio with low decompression time impact 
        2. -k option keep you original file

### Create simple and ligth docker image using **distroless.docker** docker file

    docker build -t your_image_name -f distroless.docker .

### Using dive tool to see layers of your image

    dive your_image_name

# References

* https://iximiuz.com/en/posts/containers-distroless-images/
* https://upx.github.io/
* https://thenewstack.io/dive-a-simple-app-for-viewing-the-contents-of-a-docker-image/
* https://github.com/wagoodman/dive
* https://github.com/upx/upx
* https://github.com/graalvm/native-build-tools
* https://medium.com/graalvm/compressed-graalvm-native-images-4d233766a214

