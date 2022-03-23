package com.example.core.model.socialmedia

import com.example.core.utils.DefaultCtor
import com.restfb.Facebook

@DefaultCtor
data class PageAuthenticateDto(
    @Facebook("id")
    val id: Long,

    @Facebook("access_token")
    val token: String,

    @Facebook("name")
    val name: String,

    @Facebook("instagram_business_account")
    val instagramId: Long?
) {
}