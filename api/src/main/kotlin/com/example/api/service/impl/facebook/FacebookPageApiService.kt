package com.example.api.service.impl.facebook

import com.example.api.dto.*
import com.example.api.dto.comment.CommentDto
import com.example.api.dto.message.MessageDto
import com.example.api.dto.message.SendMessageDto
import com.example.api.events.ConversationGetMessagesEvent
import com.example.api.events.PostRequestedEvent
import com.example.api.mapper.CommentsMapper
import com.example.api.mapper.ConversationMapper
import com.example.api.service.SocialMediaCommenting
import com.example.api.service.SocialMediaConversation
import com.example.api.service.SocialMediaPosting
import com.example.core.annotation.Logger
import com.example.core.dto.PostDto
import com.example.core.dto.PostResponseDto
import com.example.core.dto.PublishPostDto
import com.example.core.mapper.PublishPostDtoMapper
import com.example.core.model.SocialMedia
import com.example.core.service.FacebookApi
import com.restfb.BinaryAttachment
import com.restfb.FacebookClient
import com.restfb.Parameter
import com.restfb.json.JsonObject
import com.restfb.types.Comment
import com.restfb.types.Conversation
import com.restfb.types.send.IdMessageRecipient
import com.restfb.types.send.MediaAttachment
import com.restfb.types.send.Message
import com.restfb.types.send.SendResponse
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
@Logger
class FacebookPageApiService(
    private val publishPostMapper: PublishPostDtoMapper,
    private val conversationMapper: ConversationMapper,
    private val commentsMapper: CommentsMapper,
    private val eventPublisher: ApplicationEventPublisher,
) : SocialMediaPosting, FacebookApi, SocialMediaConversation, SocialMediaCommenting {

    override fun getPosts(socialMedia: SocialMedia): List<PostDto> =
        doFacebookClientAction(socialMedia.token, socialMedia.nativeId.toString()) { client, id ->
            client.fetchConnection(
                """$id/feed""",
                PostDto::class.java
            ).data.onEach { it.socialMediaType = socialMedia.socialMediaType }
                .also {
                    eventPublisher.publishEvent(PostRequestedEvent(socialMedia, it))
                }
        }

    override fun publishPost(socialMedia: SocialMedia, postDto: PublishPostDto): PostDto {
        return doFacebookClientAction(socialMedia.token, socialMedia.nativeId.toString()) { client, id ->
            val responseDto = postDto.attachment?.let { publishMediaPost(client, postDto, id) }
                ?: publishTextPost(client, postDto, id)
            client.fetchObject(responseDto.postId, PostDto::class.java).apply {
                this.pageId = socialMedia.nativeId ?: -1
                this.socialMediaType = socialMedia.socialMediaType
            }
                ?: throw Exception("Something went wrong, post id is ${responseDto.postId}")
        }
    }

    private fun publishMediaPost(client: FacebookClient, postDto: PublishPostDto, pageId: String) = client.publish(
        """$pageId/feed""",
        PostResponseDto::class.java,
        BinaryAttachment.with(postDto.attachment!!.name, postDto.attachment!!.bytes, postDto.attachment!!.contentType),
        Parameter.with("message", postDto.content)
    )

    private fun publishTextPost(client: FacebookClient, postDto: PublishPostDto, pageId: String) =
        client.publish(
            """$pageId/feed""",
            PostResponseDto::class.java,
            Parameter.with("message", postDto.content)
        )

    override fun getAllConversations(socialMedia: SocialMedia): List<ConversationDto> =
        doFacebookClientAction(socialMedia.token, socialMedia.nativeId.toString()) { client, id ->
            val restFbConversations = client.fetchConnection(
                """$id/conversations""",
                Conversation::class.java,
                Parameter.with("fields", "participants,message_count,can_reply,id,updated_time")
            ).data

            restFbConversations.map {
                conversationMapper.convertRestFbConversationToDto(it)
            }
        }

    override fun getConversationWithMessages(
        socialMedia: SocialMedia,
        conversationId: String
    ): ConversationWithMessagesDto = doFacebookClientAction(socialMedia.token, conversationId) { client, id ->
        val restFbConversationWithMessages = client.fetchObject(
            id, Conversation::class.java,
            Parameter.with("fields", "messages{message,attachments,from,created_time},message_count,can_reply")
        )

        conversationMapper.convertRestFbConversationToConversationWithMessagesDto(restFbConversationWithMessages)
            .also {
                val messageEntities = conversationMapper.convertRestFbConversationToMessages(it, socialMedia)
                eventPublisher.publishEvent(ConversationGetMessagesEvent(messageEntities))
            }
    }

    override fun sendMessageToConversation(
        socialMedia: SocialMedia,
        sendMessageDto: SendMessageDto,
        file: MultipartFile?
    ): MessageDto = doFacebookClientAction(socialMedia.token, socialMedia.nativeId.toString()) { client, id ->
        val recipient = IdMessageRecipient(sendMessageDto.recipientId)

        val testMessage = sendMessageDto.message?.let {
            val response = publishTextMessage(client, recipient, id, Message(it))
            val facebookMessage =
                fetchFacebookMessageWithFields(client, response.messageId, "message,created_time,from")
            return@let conversationMapper.convertRestFbMessageToMessageDto(facebookMessage)
        }

        val mediaMessage = sendMessageDto.attachment?.let {
            //todo: workout files properly
            val mediaAttachment = MediaAttachment(MediaAttachment.Type.IMAGE)
            val response =
                publishMediaMessage(id, client, prepareBinaryAttachment(it), recipient, Message(mediaAttachment))
            val facebookMessage =
                fetchFacebookMessageWithFields(client, response.messageId, "attachments,created_time,from")
            return@let conversationMapper.convertRestFbMessageToMessageDto(facebookMessage)
        }

        when {
            testMessage != null -> testMessage.apply {
                this.attachment = conversationMapper.convertMessageDtoToReceiveAttachment(mediaMessage)
            }
            mediaMessage != null -> mediaMessage.apply {
                this.nativeId = this.attachment?.nativeId!!
            }
            else -> throw Exception("No text or media was provided")
        }
    }

    private fun prepareBinaryAttachment(file: MultipartFile) =
        BinaryAttachment.with(file.name, file.bytes, org.springframework.http.MediaType.IMAGE_PNG_VALUE)

    private fun publishMediaMessage(
        nativeId: String,
        facebookClient: FacebookClient,
        binaryAttachment: BinaryAttachment,
        recipient: IdMessageRecipient,
        message: Message
    ) = facebookClient.publish(
        """${nativeId}/messages""",
        SendResponse::class.java, binaryAttachment,
        Parameter.with("recipient", recipient),
        Parameter.with("message", message)
    )

    private fun fetchFacebookMessageWithFields(
        facebookClient: FacebookClient,
        messageId: String,
        fieldsString: String
    ) = facebookClient.fetchObject(
        messageId,
        com.restfb.types.Message::class.java,
        Parameter.with("fields", fieldsString)
    )

    private fun publishTextMessage(
        facebookClient: FacebookClient,
        recipient: IdMessageRecipient,
        socialMediaId: String,
        message: Message
    ) = facebookClient.publish(
        """${socialMediaId}/messages""",
        SendResponse::class.java,
        Parameter.with("recipient", recipient),
        Parameter.with("message", message)
    )

    override fun getPostComments(socialMedia: SocialMedia, postId: String): List<CommentDto> =
        doFacebookClientAction(socialMedia.token, postId) { client, id ->
            fetchComments(client, id).data.map { commentsMapper.mapToCommentDto(it) }
        }

    private fun fetchComments(client: FacebookClient, postId: String) =
        client.fetchConnection(
            """$postId/comments""",
            Comment::class.java,
            Parameter.with("fields", "created_time,id,attachment,from{name,id},message")
        )

    override fun publishComment(socialMedia: SocialMedia, postId: String, commentDto: PublishCommentDto): CommentDto =
        doFacebookClientAction(socialMedia.token, postId) { client, id ->
            val response = publishComment(client, postId, commentDto) ?: throw Exception("failed to send a message")
            commentsMapper.mapToCommentDto(fetchComment(client, response.getString("id", null)))
        }

    private fun publishComment(client: FacebookClient, postId: String, commentDto: PublishCommentDto): JsonObject? =
        client.publish(
            """$postId/comments""",
            JsonObject::class.java,
            *prepareParams(commentDto).toTypedArray()
        )

    private fun fetchComment(client: FacebookClient, id: String) = client.fetchObject(
        id, JsonObject::class.java, Parameter.with("fields", "created_time,id,attachment,from{name,id},message")
    )

    private fun prepareParams(commentDto: PublishCommentDto): List<Parameter?> {
        return listOfNotNull(
            commentDto.content?.let { Parameter.with("message", it) },
            commentDto.attachment?.let { Parameter.with("source", it) })
    }

    override fun respondToComment(
        socialMedia: SocialMedia,
        commentId: String,
        commentDto: PublishCommentDto
    ): CommentDto {
        TODO("Not yet implemented")
    }
}