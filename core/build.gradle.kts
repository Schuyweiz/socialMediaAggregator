import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("socialAggregator.kotlin-common-conventions")
    id("org.springframework.boot")
    //not rewqioire any args in kotlin
    id("org.jetbrains.kotlin.plugin.noarg")
    kotlin("kapt")
    kotlin("plugin.spring")
    kotlin("jvm")
    id("org.jetbrains.kotlin.plugin.lombok")
}

repositories {
    maven {
        url = uri("https://repo.spring.io/release")
    }
}

//no args for entitiy
configure<org.jetbrains.kotlin.noarg.gradle.NoArgExtension> {
    annotation("javax.persistence.Entity")
}
java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(11))

    java.sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
        getByName("test").java.srcDirs("src/test/kotlin")
    }
    val files: Set<File> = sourceSets["main"].java.srcDirs
    println("app src java: $files")

}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    //should neve be updated
    implementation("org.glassfish.jaxb:jaxb-runtime:2.3.3")
    annotationProcessor("org.glassfish.jaxb:jaxb-runtime:2.3.3")
    annotationProcessor("jakarta.xml.bind:jakarta.xml.bind-api:2.3.3")
    annotationProcessor("javax.annotation:javax.annotation-api:1.3.2")

    implementation("jakarta.xml.bind:jakarta.xml.bind-api:2.3.3")
    //

    //validation
    implementation("org.springframework.boot:spring-boot-starter-validation")

    //lombok
    compileOnly("org.projectlombok:lombok")

    // https://mvnrepository.com/artifact/org.postgresql/postgresql
    implementation("org.postgresql:postgresql")

    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-gradle-plugin
    implementation("org.springframework.boot:spring-boot-gradle-plugin")

    // https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-webflux
    implementation("org.springframework.boot:spring-boot-starter-webflux")


    // testing ---------------------------------------------------------------------------------------------------------

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation("com.nhaarman:mockito-kotlin")

    runtimeOnly("io.kotest:kotest-assertions-core-jvm")

    testImplementation("io.kotest:kotest-runner-junit5-jvm")
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

tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false
}

tasks.getByName<Jar>("jar") {
    enabled = true
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
}