package com.example.api.mapper

import com.example.api.dto.ConversationDto
import com.restfb.types.Conversation as RestFbConversation
import org.springframework.stereotype.Component

@Component
class ConversationMapper {

    fun convertRestFbConversationToDto(restFbConversation: RestFbConversation): ConversationDto = ConversationDto(
        conversationId = restFbConversation.id,
        updatedTime = restFbConversation.updatedTime,
        messageCount = restFbConversation.messageCount,
        unreadCount = restFbConversation.unreadCount,
        participants = restFbConversation.participants,
        canReply = restFbConversation.canReply,
    )
}