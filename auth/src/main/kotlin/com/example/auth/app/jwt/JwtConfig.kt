package com.example.auth.app.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.JWTVerifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class JwtConfig {
    //fixme: replace secret with somerthing more useful
    @Bean
    fun algorithm(): Algorithm = Algorithm.HMAC256("secret")

    @Bean
    fun jwtVerifier(): JWTVerifier = JWT.require(algorithm()).build()
}