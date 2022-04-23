package com.example.api.dto

import com.example.core.annotation.DefaultCtor
import org.springframework.web.multipart.MultipartFile

@DefaultCtor
data class PublishCommentDto(
    val content: String?,
    val attachment: MultipartFile?,
) {
}