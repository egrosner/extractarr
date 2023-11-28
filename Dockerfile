FROM eclipse-temurin:17-jdk-jammy
LABEL authors="karby"

VOLUME /watch

RUN apt update -y && apt install p7zip-full -y

COPY entrypoint.sh .
RUN ["chmod", "+x", "entrypoint.sh"]

COPY /build/libs/*.jar app.jar

ENTRYPOINT ["./entrypoint.sh"]