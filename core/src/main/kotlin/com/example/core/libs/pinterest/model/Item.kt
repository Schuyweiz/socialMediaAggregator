package com.example.core.libs.pinterest.model

data class Item(
    val description: String?,
    val id: String,
    val name: String,
    val owner: Owner,
    val privacy: String
)