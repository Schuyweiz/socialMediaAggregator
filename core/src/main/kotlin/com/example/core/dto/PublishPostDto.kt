package com.example.core.dto

import com.example.core.annotation.DefaultCtor
import org.springframework.web.multipart.MultipartFile

@DefaultCtor
data class PublishPostDto(
    val content: String,
    val attachment: MultipartFile?,
    val pinBoardId: String?,
    val pinSectionId: String?,
    val pinTitle: String?,
)