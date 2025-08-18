# Platform
This monorepo hosts a number of Spring Boot Dockerisable microservices and commons libraries, that constitute the platform for a Unified Namespace-powered eCommerce enterprise that is digitally transforming/transformed. Continuous Integration is performed by GitHub Actions. This project consists only of backend services. No frontend.


# Features
1. reusable lifecycle management service of entities


# Build and Run
To build and install a library (not a service) to local maven run
```shell
$ cd core
$ ./gradlew clean publishToMavenLocal
```


To build a project run
```shell
$ ./gradlew --stop
$ ./gradlew clean build --refresh-dependencies
```


To build a spring boot app as a JAR run
```shell
$ ./gradlew clean bootJar
```


To run a specific spring boot app run
```shell
$ java -jar services/service1/build/libs/app.jar
```


To run a particular service (e.g. service1) using Docker run:
```shell
$ cd workspaces/my-project/services/service1
$ docker-compose up --build
```


To generate the OpenAPI JSON for the API documentation of project service1 then:
```shell
$ cd workspaces/my-project/services/service1
$ ./gradlew clean generateOpenApiDocs
```
This generates the file /workspaces/open-source-projects/platform/services/service1/build/openapi/openapi.json 


To generate the Java client SDK for project service1:
```shell
$ cd workspaces/my-project/services/service1-sdk
$ ./gradlew openApiGenerate
```
This generates the source code of the maven-based Java-based client SDK for the API of the project service1 in the folder services/service1-sdk/generated


To build the shell app run
```shell
$ cd workspaces/my-project/uns-cli
$ ./gradlew clean build
$ java -jar build/libs/uns-cli-0.0.1.jar say-hello Jimmy
```
