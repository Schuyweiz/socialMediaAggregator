package com.example.api.dto

import com.example.core.libs.pinterest.model.BoardOwner
import com.example.core.libs.pinterest.model.Media

data class CreatePinResponse(
    val alt_text: String,
    val board_id: String,
    val board_owner: BoardOwner,
    val board_section_id: String,
    val created_at: String,
    val description: String,
    val id: String,
    val link: String,
    val media: Media,
    val title: String
)