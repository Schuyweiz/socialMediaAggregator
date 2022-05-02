package com.example.core.libs.pinterest.model

data class CreatePinterestBoardRequest(
    val description: String?,
    val name: String,
    val privacy: String?
)