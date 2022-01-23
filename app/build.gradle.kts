import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("socialAggregator.kotlin-application-conventions")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("org.jetbrains.kotlin.plugin.noarg")
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("kapt")
}

noArg {
    annotation("javax.persistence.Entity")
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}
java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
    val ktl = kotlin.sourceSets["main"].kotlin.srcDirs
    val files: Set<File> = sourceSets["main"].java.srcDirs
    println("app src java: $files")
    println("app src kotlin: $ktl")
}

dependencies {

    implementation( project(":auth"))
    implementation( project(":core"))

    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.liquibase:liquibase-core")
    implementation("org.springframework.boot:spring-boot-starter-security:2.6.2")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    compileOnly("org.projectlombok:lombok")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.projectlombok:lombok")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.6.2")
    implementation("org.springframework.boot:spring-boot-starter-validation:2.6.2")

    implementation(kotlin("stdlib-jdk8"))
    implementation("org.mapstruct:mapstruct:1.5.0.Beta2")
    kapt("org.mapstruct:mapstruct-processor:1.5.0.Beta2")

    // configs for testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("com.nhaarman:mockito-kotlin:1.6.0")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.0.3")
    testImplementation("org.mockito:mockito-junit-jupiter:4.2.0")
    runtimeOnly("io.kotest:kotest-assertions-core-jvm:5.0.3")

    // configs for integration testing
    // https://mvnrepository.com/artifact/org.testcontainers/postgresql
    testImplementation("org.testcontainers:postgresql:1.16.2")
// https://mvnrepository.com/artifact/org.testcontainers/testcontainers
    testImplementation("org.testcontainers:testcontainers:1.16.2")
    // https://mvnrepository.com/artifact/org.testcontainers/junit-jupiter
    testImplementation("org.testcontainers:junit-jupiter:1.16.2")
// https://mvnrepository.com/artifact/org.postgresql/postgresql
    implementation("org.postgresql:postgresql:42.3.1")

    // https://mvnrepository.com/artifact/com.auth0/java-jwt
    implementation("com.auth0:java-jwt:3.18.3")


}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

application{
    mainClass.set("com.example.socialmediaaggregator.SocialMediaAggregatorApplication")
}

tasks.bootJar{
    enabled = true
}