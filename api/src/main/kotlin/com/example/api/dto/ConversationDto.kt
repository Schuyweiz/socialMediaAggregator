package com.example.api.dto

import com.example.core.annotation.DefaultCtor
import com.restfb.Facebook
import com.restfb.types.ExtendedReferenceType
import java.util.*

@DefaultCtor
data class ConversationDto(
    @Facebook("id")
    val conversationId: String,
    @Facebook("updated_time")
    val updatedTime: Date?,
    val createdTime: Date?,
    @Facebook("message_count")
    val messageCount: Long,
    @Facebook("unread_count")
    val unreadCount: Long,
    @Facebook("participants")
    val participants: List<ExtendedReferenceType>,
    @Facebook("can_reply")
    val canReply: Boolean,
) {
}