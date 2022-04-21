package com.example.api.dto

import com.example.core.annotation.DefaultCtor
import com.restfb.Facebook
import com.restfb.types.ExtendedReferenceType
import java.util.*

@DefaultCtor
data class ConversationDto(
    @Facebook("id")
    val conversationId: String? = null,
    @Facebook("updated_time")
    val updatedTime: Date?,
    val createdTime: Date?,
    @Facebook("message_count")
    val messageCount: Long? = null,
    @Facebook("unread_count")
    val unreadCount: Long? = null,
    @Facebook("participants")
    val participants: List<ExtendedReferenceType>,
    @Facebook("can_reply")
    val canReply: Boolean? = null,
) {
}