package com.example.api.dto

import com.example.core.annotation.DefaultCtor

@DefaultCtor
data class CommentDto(
    var id: Long?,
    val nativeId: String,
    val content: String?,
    val senderDto: SenderDto
) {
}