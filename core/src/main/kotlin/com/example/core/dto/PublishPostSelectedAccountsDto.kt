package com.example.core.dto

import com.example.core.annotation.DefaultCtor

@DefaultCtor
data class PublishPostSelectedAccountsDto(
    val publishPostDto: PublishPostDto,
    val socialMediaIds: Set<Long>,
) {
}