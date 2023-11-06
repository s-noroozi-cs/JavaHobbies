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
