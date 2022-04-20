package com.example.core.dto

import com.example.core.model.SocialMediaType
import com.example.core.annotation.DefaultCtor
import com.restfb.Facebook

@DefaultCtor
data class PostDto(
    @Facebook("id")
    val postId: String,
    @Facebook("message")
    val content: String?,
    @Facebook("likes")
    val likesCount: Long,

    var pageId: Long,
    var mediaUrl: String? = null,
    var socialMediaType: SocialMediaType
) {
}