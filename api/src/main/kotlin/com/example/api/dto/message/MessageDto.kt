package com.example.api.dto.message

import com.example.api.dto.ParticipantDto
import com.example.api.dto.RecieveAttachmentDto
import com.example.core.annotation.DefaultCtor
import java.util.*

@DefaultCtor
data class MessageDto(
    var nativeId: String,
    var message: String?,
    val participantDto: ParticipantDto,
    val createdTime: Date,
    var attachment: RecieveAttachmentDto?,
    )