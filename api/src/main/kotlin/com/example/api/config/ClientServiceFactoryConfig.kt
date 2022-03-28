package com.example.api.config

import org.springframework.beans.factory.config.ServiceLocatorFactoryBean
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class ClientServiceFactoryConfig {

    @Bean
    fun postServiceFactoryBean(): ServiceLocatorFactoryBean = ServiceLocatorFactoryBean().apply {
        setServiceLocatorInterface(PostServiceRegistry::class.java)
    }

    @Bean
    fun conversationServiceFactoryBean(): ServiceLocatorFactoryBean = ServiceLocatorFactoryBean().apply {
        setServiceLocatorInterface(ConversationServiceRegistry::class.java)
    }
}