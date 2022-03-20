package com.example.core.model.socialmedia

import com.example.core.utils.DefaultCtor
import com.restfb.Facebook
import com.restfb.types.Page

@DefaultCtor
data class FacebookPage(
    @Facebook("id")
    val pageId: Long,
    @Facebook("name")
    val pageName: String,
    @Facebook("access_token")
    val accessToken: String,
) {
}