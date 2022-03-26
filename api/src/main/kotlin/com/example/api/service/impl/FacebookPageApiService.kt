package com.example.api.service.impl

import com.example.api.events.PostRequestedEvent
import com.example.api.service.FacebookApi
import com.example.api.service.SocialMediaPosting
import com.example.core.annotation.Logger
import com.example.core.dto.PostDto
import com.example.core.dto.PostResponseDto
import com.example.core.dto.PublishPostDto
import com.example.core.mapper.PublishPostDtoMapper
import com.example.core.model.SocialMedia
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
@Logger
class FacebookPageApiService(
    private val publishPostMapper: PublishPostDtoMapper,
    private val eventPublisher: ApplicationEventPublisher,
) : SocialMediaPosting, FacebookApi {
    override fun getPosts(socialMedia: SocialMedia): List<PostDto> {
        val facebookClient = getFacebookClient(socialMedia.token)
        val pageId = socialMedia.nativeId

        return facebookClient.fetchConnection("""$pageId/feed""", PostDto::class.java).data.apply {
            forEach { it.socialMediaType = socialMedia.socialMediaType }
        }.also {
            eventPublisher.publishEvent(PostRequestedEvent(socialMedia, it))
        }
    }


    override fun publishPost(socialMedia: SocialMedia, postDto: PublishPostDto): PostDto {
        val facebookClient = getFacebookClient(socialMedia.token)
        val pageId = socialMedia.nativeId

        val responseDto = facebookClient.publish(
            """$pageId/feed""",
            PostResponseDto::class.java,
            *publishPostMapper.mapToFacebookParams(postDto)
        )

        return facebookClient.fetchObject(responseDto.postId, PostDto::class.java).apply {
            this.pageId = socialMedia.nativeId ?: -1
            this.socialMediaType = socialMedia.socialMediaType
        }
            ?: throw Exception("Something went wrong, post id is ${responseDto.postId}")
    }
}