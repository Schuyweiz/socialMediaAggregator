package com.example.api.dto

data class Image(
    val extension: String,
    val filename: String,
    val mime: String,
    val name: String,
    val url: String
)