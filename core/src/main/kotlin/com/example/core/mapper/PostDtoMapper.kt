package com.example.core.mapper

import com.example.core.model.SocialMedia
import com.example.core.model.socialmedia.Post
import com.example.core.dto.PostDto
import com.restfb.json.JsonObject
import org.springframework.stereotype.Component
import  com.restfb.types.Post as RestFbPost

@Component
class PostDtoMapper {

    fun mapToPost(dto: PostDto, socialMedia: SocialMedia) = Post(textContent = dto.content, socialMedia = socialMedia)

    fun mapRestFbPostToPostDto(json: JsonObject, socialMedia: SocialMedia) = PostDto(
        postId = json.getString("id", ""),
        content = json.getString("message", ""),
        likesCount = json.getLong("likes_count", 0L),
        pageId = socialMedia.nativeId!!,
        socialMediaType = socialMedia.socialMediaType,
    )
}