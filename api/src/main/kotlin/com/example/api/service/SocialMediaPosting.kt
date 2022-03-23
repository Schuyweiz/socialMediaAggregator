package com.example.api.service

import com.example.core.model.SocialMedia
import com.example.core.model.socialmedia.Post
import com.example.core.model.socialmedia.PostDto
import com.example.core.model.socialmedia.PublishPostDto

interface SocialMediaPosting {

    fun getPosts(socialMedia: SocialMedia): List<PostDto>

    fun publishPost(socialMedia: SocialMedia, postDto: PublishPostDto): List<PostDto>
}