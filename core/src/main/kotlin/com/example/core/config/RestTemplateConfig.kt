package com.example.core.config

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class RestTemplateConfig {

    //TODO: look into configuration  https://www.baeldung.com/rest-template
    @Bean
    fun restTemplate() = RestTemplate()

}