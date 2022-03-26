package com.example.core.dto

import com.example.core.annotation.DefaultCtor

@DefaultCtor
data class LoginRequestDto(
    val username:String,
    val password: String
) {
}