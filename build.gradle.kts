plugins {
    id("org.springframework.boot") version "2.6.3"
    //not rewqioire any args in kotlin
    id("org.jetbrains.kotlin.plugin.noarg") version "1.6.10"
    kotlin("kapt")
    id("org.jetbrains.kotlin.plugin.lombok") version "1.6.10"
    id("org.jetbrains.kotlin.plugin.spring") version "1.6.10"
    id("nebula.spring-boot-jar") version "18.2.0"

}

allprojects {

    repositories {
        google()
        mavenCentral()

    }

    group = "com.example"
    version = "0.0.1-SNAPSHOT"
}

subprojects {

    apply(plugin = "java")
    apply(plugin = "org.jetbrains.kotlin.plugin.lombok")

    repositories {
        mavenCentral()
    }
}

tasks.withType<org.springframework.boot.gradle.tasks.run.BootRun> {
    mainModule.set(":app")
    mainClass.set("com.example.socialmediaaggregator.SocialMediaAggregatorApplication")
}
