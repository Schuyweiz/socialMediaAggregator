
plugins {
    id("socialAggregator.kotlin-common-conventions")
    id("org.springframework.boot") apply(false)
    kotlin("plugin.spring")
}
dependencies {

    //project dependencies
    implementation(project(":core"))

    //region spring dependencies

    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.springframework.security:spring-security-core")

    implementation("org.springframework.boot:spring-boot-starter-mail")

    implementation("org.springframework.security:spring-security-config")

    implementation("org.springframework.security:spring-security-web")

    implementation("org.springframework.security.oauth:spring-security-oauth2")

    implementation("org.springframework.boot:spring-boot-gradle-plugin")

    implementation("org.springframework.security:spring-security-oauth2-client")

    //endregion

    implementation("org.springdoc:springdoc-openapi-data-rest:1.6.0")

    // https://mvnrepository.com/artifact/org.springdoc/springdoc-openapi-security
    implementation("org.springdoc:springdoc-openapi-security")



    implementation("com.auth0:java-jwt")

    implementation("com.restfb:restfb:2022.3.1")

    implementation("com.vk.api:sdk:1.0.14")
    // https://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient
    implementation("org.apache.httpcomponents:httpclient:4.5.2")



}

tasks.withType<Test> {
    useJUnitPlatform()
}

