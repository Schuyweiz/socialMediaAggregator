package com.example.core.model.socialmedia

import com.example.core.utils.DefaultCtor
import com.restfb.Facebook

@DefaultCtor
data class PostResponseDto(
    @Facebook("success")
    val success: Boolean,

    @Facebook("id")
    val postId: String
) {
}