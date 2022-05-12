package com.example.tms.model

import com.example.core.model.socialmedia.SocialMediaType
import java.time.Instant

data class PostDto(
    val nativeId: String,
    val textContent: String,
    val socialMediaType: SocialMediaType,
    val likes: Long,
    val comments: Long,
    val createdAt: Instant,
) {
}