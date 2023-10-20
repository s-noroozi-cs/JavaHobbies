FROM  ghcr.io/graalvm/jdk:java17
WORKDIR /app
COPY . /app
ENV SDKMAN_DIR /root/.sdkman
ENV JAVA_VERSION 22.3.r17-nik
EXPOSE 8080
SHELL ["/bin/bash", "-c"]
RUN microdnf install --nodocs openssl util-linux unzip zip
RUN microdnf install -y wget && microdnf install -y tar &&  microdnf install -y gzip && microdnf install -y maven && microdnf install gcc glibc-devel zlib-devel
RUN curl -s "https://get.sdkman.io" | bash
WORKDIR $SDKMAN_DIR
RUN [[ -s "$SDKMAN_DIR/bin/sdkman-init.sh" ]] && source "$SDKMAN_DIR/bin/sdkman-init.sh" && exec "$@"
RUN source /root/.bashrc
RUN source "$SDKMAN_DIR/bin/sdkman-init.sh" && sdk install java $JAVA_VERSION && sdk use java $JAVA_VERSION && cd /app && mvn -Pnative native:compile
WORKDIR /app
ENTRYPOINT ["./target/native-compile", "--spring.profiles.active=local"]