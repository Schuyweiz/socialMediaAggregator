import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("socialAggregator.kotlin-common-conventions")
    id("org.springframework.boot") apply(false)
    kotlin("plugin.spring")
}
dependencies {

    //project dependencies
    implementation(project(":core"))

    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web
    implementation("org.springframework.boot:spring-boot-starter-web")

    // https://mvnrepository.com/artifact/org.springframework.security/spring-security-core
    implementation("org.springframework.security:spring-security-core")
// https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-mail
    implementation("org.springframework.boot:spring-boot-starter-mail")

// https://mvnrepository.com/artifact/com.auth0/java-jwt
    implementation("com.auth0:java-jwt")

    // https://mvnrepository.com/artifact/org.springframework.security/spring-security-config
    implementation("org.springframework.security:spring-security-config")

    implementation("org.springframework.boot:spring-boot-starter-webflux")


    // https://mvnrepository.com/artifact/org.springframework.security/spring-security-web
    implementation("org.springframework.security:spring-security-web")

    // https://mvnrepository.com/artifact/org.springframework.security.oauth/spring-security-oauth2
    implementation("org.springframework.security.oauth:spring-security-oauth2")

    implementation("org.springframework.boot:spring-boot-gradle-plugin")
    implementation("org.springframework.security:spring-security-oauth2-client")
    implementation("com.restfb:restfb:2022.3.1")

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

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
}
