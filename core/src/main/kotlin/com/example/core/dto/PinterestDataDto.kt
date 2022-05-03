package com.example.core.dto

import com.fasterxml.jackson.annotation.JsonAutoDetect

@JsonAutoDetect
data class PinterestDataDto(
    val boardId: Long,
    val sectionId: Long,
    val title: String,
) {
}