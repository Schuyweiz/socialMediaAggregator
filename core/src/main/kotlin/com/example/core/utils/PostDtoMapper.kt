package com.example.core.utils

import com.example.core.model.SocialMedia
import com.example.core.model.socialmedia.Post
import com.example.core.model.socialmedia.PostDto
import org.springframework.stereotype.Component

@Component
class PostDtoMapper {

    fun mapToPost(dto: PostDto, socialMedia: SocialMedia) = Post(textContent = dto.content, socialMedia = socialMedia)
}