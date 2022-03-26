package com.example.api.service

import com.example.core.model.SocialMedia
import com.example.core.dto.PostDto
import com.example.core.dto.PublishPostDto

interface SocialMediaPosting {

    fun getPosts(socialMedia: SocialMedia): List<PostDto>

    fun publishPost(socialMedia: SocialMedia, postDto: PublishPostDto): PostDto
}