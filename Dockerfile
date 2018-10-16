FROM openjdk:8-slim
ARG JAR_FILE
COPY ${JAR_FILE} jmasar.jar
COPY entrypoint.sh /usr/local/bin
ENTRYPOINT ["/usr/local/bin/entrypoint.sh" ]
