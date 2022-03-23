package com.example.core.model.socialmedia

import com.example.core.model.SocialMediaType
import com.example.core.utils.DefaultCtor

@DefaultCtor
data class SocialMediaDto(
    val nativeId: Long?,
    val token: String,
    val socialMediaType: SocialMediaType,
) {
}