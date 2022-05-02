package com.example.core.libs.pinterest.model

data class PinterestAuthneticationData(
    val access_token: String,
    val expires_in: Int,
    val refresh_token: String,
    val refresh_token_expires_in: Int,
    val response_type: String,
    val scope: String,
    val token_type: String
)