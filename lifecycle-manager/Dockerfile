FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app
COPY gradlew .
COPY gradle ./gradle
RUN chmod +x gradlew
COPY settings.gradle.kts build.gradle.kts ./
COPY core core
COPY lifecycle-manager ./lifecycle-manager
RUN ./gradlew :lifecycle-manager:bootJar --no-daemon
FROM eclipse-temurin:21-jre-jammy
EXPOSE 8080
COPY --from=build /app/lifecycle-manager/build/libs/app.jar /app.jar
ARG LIFECYELE_DATABASE_URL
ARG LIFECYELE_DATABASE_USERNAME
ARG LIFECYELE_DATABASE_PASSWORD
ENTRYPOINT java -jar "/app.jar"
