package com.example.api.config

import com.example.api.service.SocialMediaConversation

interface ConversationServiceRegistry {

    fun getConversationService(serviceName: String): SocialMediaConversation
}