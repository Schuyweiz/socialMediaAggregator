package com.example.api.service

import com.example.core.model.SocialMedia
import com.example.core.model.socialmedia.Post

interface SocialMediaPosting {

    fun getPosts(token: SocialMedia): List<Post>;
}