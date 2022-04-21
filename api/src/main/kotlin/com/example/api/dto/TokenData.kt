package com.example.api.dto

data class TokenData(
    val app_id: String,
    val application: String,
    val data_access_expires_at: Int,
    val expires_at: Int,
    val is_valid: Boolean,
    val issued_at: Int,
    val profile_id: String,
    val type: String
)