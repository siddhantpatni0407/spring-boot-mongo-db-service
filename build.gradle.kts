plugins {
    id("java")
    id("org.springframework.boot") version "3.5.5"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot Starters
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

    // Jackson Datatype for Java 8 Date/Time (Instant, LocalDate, etc.)
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Logging (Slf4j is included in Spring Boot, but adding explicit bridge if needed)
    implementation("org.slf4j:slf4j-api:2.0.16")

    // Apache Commons BeanUtils
    implementation("commons-beanutils:commons-beanutils:1.9.4")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito:mockito-core:5.13.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.13.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
    systemProperty("spring.profiles.active", "test")
}

tasks.jar {
    enabled = false
}

tasks.bootJar {
    archiveFileName.set("spring-boot-mongo-db-service.jar")
    launchScript()
}
