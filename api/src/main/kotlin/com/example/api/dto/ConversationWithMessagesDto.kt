package com.example.api.dto

import com.example.api.dto.message.MessageDto
import com.example.core.annotation.DefaultCtor

@DefaultCtor
data class ConversationWithMessagesDto(
    val conversationDto: ConversationDto,
    val messagesDto: List<MessageDto>,
) {
}