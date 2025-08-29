FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app
COPY gradlew .
COPY gradle ./gradle
RUN chmod +x gradlew
COPY settings.gradle.kts build.gradle.kts ./
COPY core core
COPY store ./store
RUN ./gradlew :store:bootJar --no-daemon
FROM eclipse-temurin:21-jre-jammy
EXPOSE 8080
COPY --from=build /app/store/build/libs/app.jar /app.jar
ARG STORE_DATABASE_URL
ARG STORE_DATABASE_USERNAME
ARG STORE_DATABASE_PASSWORD
ENTRYPOINT java -jar "/app.jar"
