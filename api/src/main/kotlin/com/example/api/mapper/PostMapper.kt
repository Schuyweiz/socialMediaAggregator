package com.example.api.mapper

import com.example.core.dto.PostDto
import com.example.core.libs.pinterest.model.CreatePinResponseDto
import com.example.core.libs.pinterest.model.Pin
import com.example.core.model.socialmedia.SocialMediaType
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

    fun map(pins: List<Pin>, nativeId: Long) = pins.map { map(it, nativeId) }

    fun map(pin: Pin, nativeId: Long): PostDto =
        PostDto(
            pin.id,
            pin.title,
            0,
            nativeId,
            pin.media.images.originals.url,
            socialMediaType = SocialMediaType.PINTEREST
        )

    fun map(pinResponse: CreatePinResponseDto, nativeId: Long) =
        PostDto(
            pinResponse.id,
            pinResponse.description,
            0,
            nativeId,
            pinResponse.media.images.originals.url,
            SocialMediaType.PINTEREST,
        )
}