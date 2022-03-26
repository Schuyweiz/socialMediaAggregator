package com.example.core.dto

import com.example.core.annotation.DefaultCtor
import com.restfb.Facebook

@DefaultCtor
data class PostResponseDto(
    @Facebook("success")
    val success: Boolean,

    @Facebook("id")
    val postId: String
) {
}