package com.example.core.dto

import com.example.core.model.SocialMediaType
import com.example.core.annotation.DefaultCtor

@DefaultCtor
data class SocialMediaDto(
    val id: Long? = null,
    val nativeId: Long?,
    val token: String,
    val socialMediaType: SocialMediaType,
) {
}