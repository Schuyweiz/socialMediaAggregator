package com.example.api.dto

import com.example.core.annotation.DefaultCtor

@DefaultCtor
data class AttachmentDto(
    val nativeId: String?,
    val mimeType: String?,
    val size: Long?,
    val url: String?,
)