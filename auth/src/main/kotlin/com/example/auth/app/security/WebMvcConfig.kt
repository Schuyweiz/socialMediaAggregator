package com.example.auth.app.security

import org.springframework.context.annotation.Configuration
import org.springframework.security.web.method.annotation.AuthenticationPrincipalArgumentResolver
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport

@Configuration
class WebMvcConfig: WebMvcConfigurationSupport() {

    override fun addArgumentResolvers(argumentResolvers: MutableList<HandlerMethodArgumentResolver>) {
        val resolver = AuthenticationPrincipalArgumentResolver()
        argumentResolvers.add(resolver)
        super.addArgumentResolvers(argumentResolvers)
    }




}