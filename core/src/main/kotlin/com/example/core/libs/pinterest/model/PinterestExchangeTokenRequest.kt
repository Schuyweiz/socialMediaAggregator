package com.example.core.libs.pinterest.model

data class PinterestExchangeTokenRequest(
    val code: String,
    val nativeId: Long,
) {
}