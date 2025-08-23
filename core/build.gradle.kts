plugins {
    id("java-library")
    id("maven-publish")
    id("io.spring.dependency-management") version "1.1.7"
    id("com.vanniktech.dependency.graph.generator") version "0.7.0"
}

val springBootVersion = "3.5.4"

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootVersion")
    }
}

repositories {
    mavenLocal()
    mavenCentral()
}


java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = "io.github.orionlibs.ecommerce"
            artifactId = "core"
            version = "0.0.1"
        }
    }

    repositories {
        mavenLocal()
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    //implementation("org.springframework.boot:spring-boot-starter-actuator")
    //implementation("org.springframework.boot:spring-boot-starter-validation")
    //implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.9")
    implementation("commons-codec:commons-codec:1.19.0")
    //implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    //implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    //runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.6")
    //runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.6")
    //implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.2")
    //implementation("io.micrometer:micrometer-core")
    //implementation("io.rest-assured:rest-assured")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    implementation("org.slf4j:slf4j-api")
    implementation("ch.qos.logback:logback-classic")
    //implementation("com.mysql:mysql-connector-j:9.3.0")
    //runtimeOnly("com.h2database:h2")
    //api("com.fasterxml.jackson.core:jackson-databind")
    //api("com.fasterxml.jackson.core:jackson-annotations")
    //api("com.fasterxml.jackson.core:jackson-core")

    runtimeOnly("com.h2database:h2")
    testImplementation(platform("org.junit:junit-bom:5.13.3"))
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.assertj:assertj-core:3.27.4")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    finalizedBy("publishToMavenLocal")
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(listOf("-Xlint:unchecked", "-Werror"))
}

tasks.named("build") {
    finalizedBy("publishToMavenLocal")
}
