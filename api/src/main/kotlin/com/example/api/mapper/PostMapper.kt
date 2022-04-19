package com.example.api.mapper

import com.example.core.dto.PostDto
import com.example.core.model.SocialMedia
import com.example.core.model.SocialMediaType
import com.restfb.types.instagram.IgMedia
import org.springframework.stereotype.Component

@Component
class PostMapper() {

    fun map(igMedia: List<IgMedia>): List<PostDto> = igMedia.map { map(it) }

    fun map(igMedia: IgMedia): PostDto = PostDto(
        postId = igMedia.igId,
        content = igMedia.caption,
        likesCount = igMedia.likeCount,
        pageId = igMedia.id.toLong(),
        socialMediaType = SocialMediaType.INSTAGRAM,
        mediaUrl = igMedia.mediaUrl,
    )
}