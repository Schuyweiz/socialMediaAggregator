package com.example.api.dto

import com.example.core.annotation.DefaultCtor

@DefaultCtor
data class SenderDto(
    val id: String,
    val name: String?,
    val pictureUrl: String?,
) {
}