package com.example.api.service.impl.vk

import com.example.api.service.SocialMediaPosting
import com.example.core.dto.PostDto
import com.example.core.dto.PublishPostDto
import com.example.core.model.SocialMedia
import com.example.core.service.VkApi
import org.springframework.stereotype.Service

@Service
class VkUserApiService(

): SocialMediaPosting, VkApi {
    override fun getPosts(socialMedia: SocialMedia): List<PostDto> {
        TODO("Not yet implemented")
    }

    override fun publishPost(socialMedia: SocialMedia, postDto: PublishPostDto): PostDto {
        TODO("Not yet implemented")
    }
}