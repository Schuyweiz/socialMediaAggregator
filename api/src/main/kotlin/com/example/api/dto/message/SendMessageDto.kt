package com.example.api.dto.message

import com.example.core.annotation.DefaultCtor
import com.restfb.types.send.MessagingType
import org.springframework.web.multipart.MultipartFile

@DefaultCtor
data class SendMessageDto(
    var recipientId: String?,
    var message: String?,
    var messagingType: MessagingType?,
    var attachment: MultipartFile?,
)