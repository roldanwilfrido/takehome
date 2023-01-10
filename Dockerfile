FROM openjdk:17-jdk-slim

COPY build/libs/takehome-0.0.1-SNAPSHOT.jar takehome-api-1.0.jar

ENV APP_NAME=takehome-api

ENTRYPOINT ["java","-jar","/takehome-api-1.0.jar"]
