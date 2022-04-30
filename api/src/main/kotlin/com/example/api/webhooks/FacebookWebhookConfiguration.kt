package com.example.api.webhooks

import com.example.api.webhooks.listeners.FacebookMessagingListener
import com.example.core.repository.MessageRepository
import com.example.core.repository.SocialMediaRepository
import com.restfb.DefaultJsonMapper
import com.restfb.webhook.Webhook
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FacebookWebhookConfiguration(
    private val messageRepository: MessageRepository,
    private val socialMediaRepository: SocialMediaRepository,
) {

    @Bean
    fun facebookWebhook(): Webhook = Webhook().apply {
        this.registerListener(FacebookMessagingListener(messageRepository, socialMediaRepository))
    }

    @Bean
    fun defaultFacebookMapper() = DefaultJsonMapper()
}