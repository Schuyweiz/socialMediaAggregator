package com.example.api.service

import com.example.api.events.PostRequestedEvent
import com.example.core.model.SocialMedia
import com.example.core.model.socialmedia.PostDto
import com.example.core.model.socialmedia.PostResponseDto
import com.example.core.model.socialmedia.PublishPostDto
import com.example.core.utils.Logger
import com.example.core.utils.PublishPostDtoMapper
import com.restfb.Parameter
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


    override fun publishPost(socialMedia: SocialMedia, postDto: PublishPostDto): List<PostDto> {
        val facebookClient = getFacebookClient(socialMedia.token)
        val pageId = socialMedia.nativeId

        val responseDto = facebookClient.publish(
            """$pageId/feed""",
            PostResponseDto::class.java,
            *publishPostMapper.mapToFacebookParams(postDto)
        )

        return facebookClient.fetchConnection(responseDto.postId, PostDto::class.java)?.data
            ?: throw Exception("Something went wrong, post id is ${responseDto.postId}")
    }
}