package com.example.api.service

import com.example.core.model.SocialMediaToken
import com.example.core.model.socialmedia.Post

interface SocialMediaPosting {

    fun getPosts(token: SocialMediaToken): List<Post>;
}