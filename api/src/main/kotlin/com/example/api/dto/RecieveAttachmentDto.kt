package com.example.api.dto

import com.example.core.annotation.DefaultCtor

@DefaultCtor
data class RecieveAttachmentDto(
    val nativeId: String?,
    val mimeType: String?,
    val size: Long?,
    val url: String?,
)