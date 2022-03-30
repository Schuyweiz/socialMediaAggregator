package com.example.api.dto

import com.example.core.annotation.DefaultCtor
import com.restfb.types.send.MessagingType
import org.springframework.web.multipart.MultipartFile

@DefaultCtor
data class SendMessageDto(
    val recipientId: String,
    val attachmentDto: SendAttachmentDto,
    val messagingType: MessagingType,
) {
}