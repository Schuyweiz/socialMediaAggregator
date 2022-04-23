package com.example.api.dto.comment

import com.example.api.dto.SenderDto
import com.example.core.annotation.DefaultCtor

@DefaultCtor
data class CommentDto(
    var id: Long?,
    val nativeId: String,
    val content: String?,
    val senderDto: SenderDto?
) {
}