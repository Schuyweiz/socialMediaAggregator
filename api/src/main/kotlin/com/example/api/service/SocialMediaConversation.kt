package com.example.api.service

import com.example.api.dto.ConversationDto
import com.example.api.dto.ConversationWithMessagesDto
import com.example.api.dto.MessageDto
import com.example.api.dto.SendMessageDto
import com.example.core.model.SocialMedia
import org.springframework.web.multipart.MultipartFile

interface SocialMediaConversation {

    fun getAllConversations(socialMedia: SocialMedia): List<ConversationDto>

    fun getConversationWithMessages(socialMedia: SocialMedia, conversationId: String): ConversationWithMessagesDto

    fun sendMessageToConversation(
        socialMedia: SocialMedia,
        sendMessageDto: SendMessageDto,
        file: MultipartFile?
    ): MessageDto
}