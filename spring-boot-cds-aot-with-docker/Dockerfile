FROM bellsoft/liberica-runtime-container:jdk-21-slim-musl as builder

WORKDIR /home/app
ADD spring-petclinic /home/app/spring-petclinic-main
RUN cd spring-petclinic-main && ./mvnw -Dmaven.test.skip=true clean package

FROM bellsoft/liberica-runtime-container:jre-21-slim-musl as optimizer

WORKDIR /app
COPY --from=builder /home/app/spring-petclinic-main/target/*.jar petclinic.jar
RUN java -Djarmode=tools -jar petclinic.jar extract --layers --launcher


FROM docker pull bellsoft/liberica-runtime-container:jre-21-slim-musl  

ENTRYPOINT ["java", "-Dspring.aot.enabled=true", "-XX:SharedArchiveFile=application.jsa", "org.springframework.boot.loader.launch.JarLauncher"]
COPY --from=optimizer /app/app/dependencies/ ./
COPY --from=optimizer /app/app/spring-boot-loader/ ./
COPY --from=optimizer /app/app/snapshot-dependencies/ ./
COPY --from=optimizer /app/app/application/ ./
RUN java -Dspring.aot.enabled=true -XX:ArchiveClassesAtExit=./application.jsa -Dspring.context.exit=onRefresh org.springframework.boot.loader.launch.JarLauncher
