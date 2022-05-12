package com.example.api.dto

import com.example.core.annotation.DefaultCtor
import com.restfb.Facebook

@DefaultCtor
data class ConversationParticipant(
    @Facebook("id")
    val participantId: String,

    @Facebook("name")
    val participantName: String
) {
}