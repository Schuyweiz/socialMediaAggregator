package com.example.api.dto

import com.example.core.annotation.DefaultCtor
import org.springframework.web.multipart.MultipartFile
import java.time.Instant

@DefaultCtor
data class DelayedPostDto(
    val content: String,
    val attachment: MultipartFile?,
    val pinBoardId: String?,
    val pinSectionId: String?,
    val pinTitle: String?,
    val timeToPost: Instant?,
    val socialMediaIds: Set<Long>?,
) {
}