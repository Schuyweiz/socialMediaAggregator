plugins {
    id("socialAggregator.kotlin-application-conventions")
    id("org.springframework.boot")
    kotlin("plugin.spring")
}

java {
    java.sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
        getByName("test").java.srcDirs("src/test/kotlin")
    }

}

dependencies {
    implementation("org.apache.commons:commons-text")

    //project dependencies
    implementation(project(":core"))
    implementation(project(":auth"))
    implementation(project(":api"))


    //region spring dependencies

    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.springframework.security:spring-security-core")

    implementation("org.springframework.boot:spring-boot-starter-mail")

    implementation("org.springframework.security:spring-security-web")

    implementation("org.springframework.security:spring-security-config")

    //endregion

    implementation("org.springdoc:springdoc-openapi-ui")

    implementation("org.springdoc:springdoc-openapi-kotlin")

    implementation("org.springdoc:springdoc-openapi-security")

    implementation("org.springdoc:springdoc-openapi-data-rest")

    implementation("com.auth0:java-jwt")

}

application {
    // Define the main class for the application.
    mainClass.set("com.example.socialmediaaggregator.SocialMediaAggregatorApplication")
}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootJar> {
    enabled = true
}



