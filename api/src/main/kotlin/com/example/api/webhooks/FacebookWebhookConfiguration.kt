package com.example.api.webhooks

import com.example.api.webhooks.listeners.FacebookMessagingListener
import com.example.api.webhooks.listeners.FacebookPageEventListener
import com.example.core.repository.MessageRepository
import com.example.core.repository.PostRepository
import com.example.core.repository.SocialMediaRepository
import com.restfb.DefaultJsonMapper
import com.restfb.webhook.Webhook
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FacebookWebhookConfiguration(
    private val messageRepository: MessageRepository,
    private val socialMediaRepository: SocialMediaRepository,
    private val postRepository: PostRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {

    @Bean
    fun facebookWebhook(): Webhook = Webhook().apply {
        this.registerListener(FacebookMessagingListener(messageRepository, socialMediaRepository))
        this.registerListener(FacebookPageEventListener(postRepository, applicationEventPublisher))
    }

    @Bean
    fun defaultFacebookMapper() = DefaultJsonMapper()
}