FROM eclipse-temurin:17-jdk-alpine
LABEL authors="karby"

VOLUME /watch

COPY entrypoint.sh .
RUN ["chmod", "+x", "entrypoint.sh"]

COPY /7z .
COPY /build/libs/*.jar app.jar

ENTRYPOINT ["./entrypoint.sh"]