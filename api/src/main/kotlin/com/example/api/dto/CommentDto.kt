package com.example.api.dto

import com.example.api.dto.SenderDto
import com.example.core.annotation.DefaultCtor
import java.time.Instant

@DefaultCtor
data class CommentDto(
    var id: Long?,
    val nativeId: String,
    val content: String?,
    val senderDto: SenderDto?,
    val createdTime: Instant? = null,
    val mediaId: String? = null,
) {
}