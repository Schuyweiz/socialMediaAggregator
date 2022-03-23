package com.example.core.model.socialmedia

import com.example.core.utils.DefaultCtor

@DefaultCtor
data class PublishPostDto(
    val content: String,
    val image_url: String,
) {
}