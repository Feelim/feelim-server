FROM openjdk:8-jdk-slim-buster

WORKDIR /app

COPY build/libs/feelim-0.0.1-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]