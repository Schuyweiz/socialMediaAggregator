package com.example.api.service

import com.example.api.dto.ConversationDto
import com.example.api.dto.ConversationWithMessagesDto
import com.example.core.model.SocialMedia

interface SocialMediaConversation {

    fun getAllConversations(socialMedia: SocialMedia): List<ConversationDto>

    fun getConversationWithMessages(socialMedia: SocialMedia, conversationId: String): ConversationWithMessagesDto
}