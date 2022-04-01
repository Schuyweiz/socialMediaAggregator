package com.example.api.service.impl

import com.example.api.dto.*
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
import com.restfb.BinaryAttachment
import com.restfb.Parameter
import com.restfb.types.Conversation
import com.restfb.types.send.IdMessageRecipient
import com.restfb.types.send.MediaAttachment
import com.restfb.types.send.Message
import com.restfb.types.send.SendResponse
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.Instant
import java.util.*

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
        val facebookClient = getFacebookClient(socialMedia.token)

        val recipient = IdMessageRecipient(sendMessageDto.recipientId)
        val testMessage = sendMessageDto.message?.let {
            val message = Message(it)
            val response = facebookClient.publish(
                """${socialMedia.nativeId}/messages""",
                SendResponse::class.java,
                Parameter.with("recipient", recipient),
                Parameter.with("message", message)
            )
            val facebookMessage = facebookClient.fetchObject(
                response.messageId,
                com.restfb.types.Message::class.java,
                Parameter.with("fields", "message,created_time,from")
            )
            return@let conversationMapper.convertRestFbMessageToMessageDto(facebookMessage)
        }

        val mediaMessage = sendMessageDto.attachment?.let {
            val binaryAttachment = prepareBinaryAttachment(file!!)
            //todo: workout files properly
            val mediaAttachment = MediaAttachment(MediaAttachment.Type.IMAGE)
            val message = Message(mediaAttachment)
            val response = facebookClient.publish(
                """${socialMedia.nativeId}/messages""",
                SendResponse::class.java, binaryAttachment,
                Parameter.with("recipient", recipient),
                Parameter.with("message", message)
            )
            val facebookMessage = facebookClient.fetchObject(
                response.messageId,
                com.restfb.types.Message::class.java,
                Parameter.with("fields", "attachments,created_time,from")
            )
            return@let conversationMapper.convertRestFbMessageToMessageDto(facebookMessage)
        }


        return when {
            testMessage != null -> testMessage.apply {
                this.attachment = conversationMapper.convertMessageDtoToReceiveAttachment(mediaMessage)
            }
            mediaMessage != null -> mediaMessage
            else -> throw Exception("No text or media was provided")
        }
    }

    private fun prepareBinaryAttachment(file: MultipartFile) =
        BinaryAttachment.with(file.name, file.bytes, org.springframework.http.MediaType.IMAGE_PNG_VALUE)
}