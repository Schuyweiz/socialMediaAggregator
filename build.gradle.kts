plugins {
    id("org.springframework.boot") version "2.6.3"
    //not rewqioire any args in kotlin
    id("org.jetbrains.kotlin.plugin.noarg") version "1.6.10"
    id("org.jetbrains.kotlin.plugin.spring") version "1.6.10"
    id("nebula.spring-boot-jar") version "18.2.0"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.6.0"

}
allprojects {

    repositories {
        google()
        mavenCentral()

    }

    group = "com.example"
    version = "0.0.1-SNAPSHOT"

    tasks.withType<JavaCompile> {
        sourceCompatibility = "15"
        targetCompatibility = "15"
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "15"
        }
    }
}

subprojects {

    apply(plugin = "java")

    repositories {
        mavenCentral()
    }
}

tasks.withType<org.springframework.boot.gradle.tasks.run.BootRun> {
    mainModule.set(":app")
    mainClass.set("com.example.socialmediaaggregator.SocialMediaAggregatorApplication")
}
