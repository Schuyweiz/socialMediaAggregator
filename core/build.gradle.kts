import org.jetbrains.kotlin.fir.expressions.impl.FirNoReceiverExpression.annotations
import org.jetbrains.kotlin.fir.expressions.impl.FirStubStatement.annotations
import org.jetbrains.kotlin.gradle.model.AllOpen

plugins {
    id("socialAggregator.kotlin-common-conventions")
    id("org.springframework.boot")
    //not rewqioire any args in kotlin
    id("org.jetbrains.kotlin.plugin.noarg")
    kotlin("kapt")
    kotlin("plugin.spring")
    kotlin("jvm")
    id("org.jetbrains.kotlin.plugin.allopen")
}

repositories {
    maven {
        url = uri("https://repo.spring.io/release")
    }
}

//no args for entitiy
configure<org.jetbrains.kotlin.noarg.gradle.NoArgExtension> {
    annotations("javax.persistence.Entity", "com.example.core.utils.DefaultCtor")
}
allOpen{
    annotations("javax.persistence.Entity",
        "javax.persistence.MappedSuperclass",
        "javax.persistence.Embedabble" )
}

java {
    java.sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
        getByName("test").java.srcDirs("src/test/kotlin")
    }
    val files: Set<File> = sourceSets["main"].java.srcDirs
    println("app src java: $files")

}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    //region idk why or how it works. Never to be updated. Never to be removed
    implementation("org.glassfish.jaxb:jaxb-runtime:2.3.3")
    annotationProcessor("org.glassfish.jaxb:jaxb-runtime:2.3.3")
    annotationProcessor("jakarta.xml.bind:jakarta.xml.bind-api:2.3.3")
    annotationProcessor("javax.annotation:javax.annotation-api:1.3.2")
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:2.3.3")
    //endregion

    //region spring dependencies

    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("org.springframework.boot:spring-boot-starter")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation("org.springframework.boot:spring-boot-gradle-plugin")

    implementation("org.springframework.boot:spring-boot-starter-webflux")


    testImplementation("org.springframework.boot:spring-boot-starter-test")

    //endregion

    implementation("com.restfb:restfb:2022.3.1")
    // https://mvnrepository.com/artifact/org.postgresql/postgresql
    implementation("org.postgresql:postgresql")

    testImplementation("com.nhaarman:mockito-kotlin")

    runtimeOnly("io.kotest:kotest-assertions-core-jvm")

    testImplementation("io.kotest:kotest-runner-junit5-jvm")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}