package com.example.core.model.socialmedia

import com.example.core.model.SocialMediaType
import com.example.core.utils.DefaultCtor
import com.restfb.Facebook

@DefaultCtor
data class PostDto(
    @Facebook("id")
    val postId: String,
    @Facebook("message")
    val content: String,
    @Facebook("likes")
    val likesCount: Long,

    val pageId: Long,
    var socialMediaType: SocialMediaType
) {
}