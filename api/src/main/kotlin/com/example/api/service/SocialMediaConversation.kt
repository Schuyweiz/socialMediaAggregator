package com.example.api.service

import com.example.api.dto.ConversationDto
import com.example.core.model.SocialMedia

interface SocialMediaConversation {

    fun getAllConversations(socialMedia: SocialMedia): List<ConversationDto>

}