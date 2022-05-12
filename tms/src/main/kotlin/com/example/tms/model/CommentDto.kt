package com.example.tms.model

import java.time.Instant

data class CommentDto(
    val content: String,
    val from: String,
    val createdAt: Instant,
    val postId: String,
) {
}