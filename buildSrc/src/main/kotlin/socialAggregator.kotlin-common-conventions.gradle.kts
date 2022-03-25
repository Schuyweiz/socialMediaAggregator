plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm")
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    constraints {
        // Define dependency versions as constraints
        implementation("org.apache.commons:commons-text:1.9")

        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    }

    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    //region spring dependencies

    implementation("org.springframework.boot:spring-boot-starter-validation:2.6.2")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.6.2")

    implementation("org.springframework.boot:spring-boot-starter-web:2.6.3")

    implementation("org.springframework.security:spring-security-core:5.6.1")

    implementation("org.springframework.boot:spring-boot-starter-mail:2.6.3")

    implementation("org.springframework.security:spring-security-config:5.6.1")

    implementation("org.springframework.security:spring-security-web:5.6.1")

    implementation("org.springframework.security.oauth:spring-security-oauth2:2.5.1.RELEASE")

    implementation("org.springframework.security:spring-security-oauth2-jose:5.6.1")

    implementation("org.springframework.boot:spring-boot-gradle-plugin:2.6.3")

    implementation("org.springframework.security:spring-security-oauth2-client:5.6.1")


    testImplementation("org.springframework.boot:spring-boot-starter-test:2.6.3")
    //endregion

    implementation("com.auth0:java-jwt:3.18.3")

    implementation("org.postgresql:postgresql:42.3.1")

    implementation("org.springdoc:springdoc-openapi-ui:1.6.6")

    implementation("org.springdoc:springdoc-openapi-kotlin:1.6.6")

    implementation("org.springdoc:springdoc-openapi-security:1.6.6")

    implementation("org.springdoc:springdoc-openapi-data-rest:1.6.6")


    // Use JUnit Jupiter for testing.
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.2")

    testImplementation("com.nhaarman:mockito-kotlin:1.6.0")

    testImplementation("io.kotest:kotest-runner-junit5-jvm:5.0.3")

    runtimeOnly("io.kotest:kotest-assertions-core-jvm:5.0.3")

}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}
