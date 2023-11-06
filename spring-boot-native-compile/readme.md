0. if your project managed by maven
1. mvn clean -Pnative compile spring-boot:process-aot -DskipTests
2. mvn native:compile -DskipTests
3. copy native binary compiled app from target directory
