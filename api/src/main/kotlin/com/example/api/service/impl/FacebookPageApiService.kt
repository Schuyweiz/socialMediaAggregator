package com.example.api.service.impl

import com.example.api.dto.ConversationDto
import com.example.api.dto.ConversationWithMessagesDto
import com.example.api.dto.MessageDto
import com.example.api.dto.SendMessageDto
import com.example.api.events.ConversationGetMessagesEvent
import com.example.api.events.PostRequestedEvent
import com.example.api.mapper.ConversationMapper
import com.example.api.service.FacebookApi
import com.example.api.service.SocialMediaConversation
import com.example.api.service.SocialMediaPosting
import com.example.core.annotation.Logger
import com.example.core.dto.PostDto
import com.example.core.dto.PostResponseDto
import com.example.core.dto.PublishPostDto
import com.example.core.mapper.PublishPostDtoMapper
import com.example.core.model.SocialMedia
import com.restfb.Parameter
import com.restfb.types.Conversation
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
@Logger
class FacebookPageApiService(
    private val publishPostMapper: PublishPostDtoMapper,
    private val conversationMapper: ConversationMapper,
    private val eventPublisher: ApplicationEventPublisher,
) : SocialMediaPosting, FacebookApi, SocialMediaConversation {
    override fun getPosts(socialMedia: SocialMedia): List<PostDto> {
        val facebookClient = getFacebookClient(socialMedia.token)
        val pageId = socialMedia.nativeId

        return facebookClient.fetchConnection(
            """$pageId/feed""",
            PostDto::class.java
        ).data.onEach { it.socialMediaType = socialMedia.socialMediaType }
            .also {
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

    override fun getAllConversations(socialMedia: SocialMedia): List<ConversationDto> {
        val facebookClient = getFacebookClient(socialMedia.token)
        val pageId = socialMedia.nativeId

        val restFbConversations = facebookClient.fetchConnection(
            """$pageId/conversations""",
            Conversation::class.java,
            Parameter.with("fields", "participants,message_count,can_reply,id,updated_time")
        ).data

        return restFbConversations.map {
            conversationMapper.convertRestFbConversationToDto(it)
        }
    }

    override fun getConversationWithMessages(
        socialMedia: SocialMedia,
        conversationId: String
    ): ConversationWithMessagesDto {
        val facebookClient = getFacebookClient(socialMedia.token)

        val restFbConversationWithMessages = facebookClient.fetchObject(
            conversationId, Conversation::class.java,
            Parameter.with("fields", "messages{message,attachments,from,created_time},message_count,can_reply")
        )

        return conversationMapper.convertRestFbConversationToConversationWithMessagesDto(restFbConversationWithMessages)
            .also {
                val messageEntities = conversationMapper.convertRestFbConversationToMessages(it, socialMedia)
                eventPublisher.publishEvent(ConversationGetMessagesEvent(messageEntities))
            }
    }

    override fun sendMessageToConversation(
        socialMedia: SocialMedia,
        conversationId: String,
        sendMessageDto: SendMessageDto,
        file: MultipartFile?
    ): MessageDto {

    }
}