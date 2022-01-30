package com.example.auth.registration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl

@Configuration
class RegistrationConfig {

    @Bean
    fun mailSender(): JavaMailSender = JavaMailSenderImpl().apply {
        //TODO: set up properties after creating a google project with tokens and stuff
    }
}