package com.example.core.dto

import com.example.core.annotation.DefaultCtor

@DefaultCtor
data class PublishPostDto(
    val content: String,
    val image_url: String,
) {
}