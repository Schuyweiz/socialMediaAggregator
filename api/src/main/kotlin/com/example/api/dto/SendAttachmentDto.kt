package com.example.api.dto

import com.example.core.annotation.DefaultCtor
import com.restfb.types.send.MediaAttachment.Type

@DefaultCtor
data class SendAttachmentDto(
    val message: String,
    val mediaAttachmentType: Type
) {
}