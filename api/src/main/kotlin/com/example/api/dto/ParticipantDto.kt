package com.example.api.dto

import com.example.core.annotation.DefaultCtor

@DefaultCtor
data class ParticipantDto(
    val nativeId: String,
    val name: String,
)