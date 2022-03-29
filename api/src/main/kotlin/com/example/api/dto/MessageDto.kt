package com.example.api.dto

import com.example.core.annotation.DefaultCtor
import java.util.*

@DefaultCtor
data class MessageDto(
    val nativeId: String,
    val message: String,
    val participantDto: ParticipantDto,
    val createdTime: Date,
    val attachment: AttachmentDto?,
    )