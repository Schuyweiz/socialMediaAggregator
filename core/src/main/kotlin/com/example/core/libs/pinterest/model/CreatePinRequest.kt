package com.example.core.libs.pinterest.model

data class CreatePinRequest(
    val alt_text: String,
    val board_id: String,
    val board_section_id: String,
    val description: String,
    val link: String?,
    val media_source: MediaSource,
    val title: String
)