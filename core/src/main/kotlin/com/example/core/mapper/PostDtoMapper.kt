package com.example.core.mapper

import com.example.core.model.SocialMedia
import com.example.core.model.socialmedia.Post
import com.example.core.dto.PostDto
import org.springframework.stereotype.Component

@Component
class PostDtoMapper {

    fun mapToPost(dto: PostDto, socialMedia: SocialMedia) = Post(textContent = dto.content, socialMedia = socialMedia)
}