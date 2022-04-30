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
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict", "-Xjvm-default=enable")
            jvmTarget = "1.8"
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

tasks.register("stage") {
    subprojects.forEach { project ->
        val clean = project.tasks.first { it.name.contains("clean") }
        val build = project.tasks.first { it.name.contains("build") }
        build.dependsOn(clean)
        build.mustRunAfter(clean)
        dependsOn(build)
    }
}


configurations {
    all {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
}
