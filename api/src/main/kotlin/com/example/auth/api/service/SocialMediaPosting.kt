package com.example.auth.api.service

import com.example.core.user.model.SocialMediaToken
import com.example.core.user.model.socialmedia.Post

interface SocialMediaPosting {

    fun getPosts(token: SocialMediaToken): List<Post>;
}