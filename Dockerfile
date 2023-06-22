FROM openjdk:8-jdk-slim-buster

CMD ["./gradlew", "clean", "build"]

ARG JAR_FILE_PATH=build/libs/feelim-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE_PATH} app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]