FROM  native-tool
ENV SDKMAN_DIR /root/.sdkman
ENV JAVA_VERSION 22.3.r17-nik
WORKDIR /app
COPY . /app
EXPOSE 8080
WORKDIR $SDKMAN_DIR
RUN [[ -s "$SDKMAN_DIR/bin/sdkman-init.sh" ]] && source "$SDKMAN_DIR/bin/sdkman-init.sh" && exec "$@"
RUN source /root/.bashrc
RUN source "$SDKMAN_DIR/bin/sdkman-init.sh" && sdk use java $JAVA_VERSION && cd /app && mvn clean -Pnative native:compile
WORKDIR /app
ENTRYPOINT ["./target/native-compile", "--spring.profiles.active=local"]