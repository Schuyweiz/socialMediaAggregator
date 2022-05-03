package com.example.core.libs.pinterest.model

data class CreatePinterestBoardRequest(
    val description: String? = null,
    val name: String,
    val privacy: String? = "PUBLIC"
)