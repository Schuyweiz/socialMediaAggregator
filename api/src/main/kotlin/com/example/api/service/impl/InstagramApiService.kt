package com.example.api.service.impl

import com.example.api.dto.*
import com.example.api.dto.CommentDto
import com.example.api.dto.MessageDto
import com.example.api.dto.SendMessageDto
import com.example.api.events.ConversationGetMessagesEvent
import com.example.api.events.PostRequestedEvent
import com.example.api.mapper.CommentsMapper
import com.example.api.mapper.ConversationMapper
import com.example.api.mapper.PostMapper
import com.example.api.service.SaveImageExternallyService
import com.example.api.service.SocialMediaCommenting
import com.example.api.service.SocialMediaConversation
import com.example.api.service.SocialMediaPosting
import com.example.core.annotation.Logger
import com.example.core.dto.PostDto
import com.example.core.dto.PublishPostDto
import com.example.core.model.SocialMedia
import com.example.core.service.FacebookApi
import com.restfb.DefaultFacebookClient
import com.restfb.FacebookClient
import com.restfb.Parameter
import com.restfb.Version
import com.restfb.json.JsonObject
import com.restfb.types.Conversation
import com.restfb.types.instagram.IgMedia
import com.restfb.types.send.IdMessageRecipient
import com.restfb.types.send.MediaAttachment
import com.restfb.types.send.Message
import com.restfb.types.send.SendResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.multipart.MultipartFile

@Logger
@Service
class InstagramApiService(
    private val postMapper: PostMapper,
    private val saveImageService: SaveImageExternallyService,
    private val conversationMapper: ConversationMapper,
    private val restTemplate: RestTemplate,
    private val eventPublisher: ApplicationEventPublisher,
    private val commentMapper: CommentsMapper,
    @Value("app.facebook.app-id")
    private val appId: String,
    @Value("app.facebook.app-secret")
    private val appSecret: String,
) : SocialMediaPosting, SocialMediaConversation, SocialMediaCommenting, FacebookApi {

    override fun getPosts(socialMedia: SocialMedia): List<PostDto> {
        val client = getFacebookClient(socialMedia.token)
        val nativeId = socialMedia.nativeId
        val mediaFields =
            "ig_id,children{permalink,media_type,media_url,timestamp},thumbnail_url,shortcode,timestamp,media_type,media_url,is_comment_enabled,like_count,comments{like_count,media,replies,timestamp,user,username,text},permalink,caption"

        val listMedia = client.fetchConnection(
            """$nativeId/media""", IgMedia::class.java, Parameter.with("fields", mediaFields)
        )

        return listMedia.data.map { postMapper.map(it) }
            .also { eventPublisher.publishEvent(PostRequestedEvent(socialMedia, it)) }
    }

    override fun publishPost(socialMedia: SocialMedia, postDto: PublishPostDto): PostDto {
        val client = getFacebookClient(socialMedia.token)
        val nativeId = socialMedia.nativeId
        val imageUrl = postDto.attachment?.let { saveImageService.saveImage(it) }?:saveImageService.saveImage(postDto.byteContent!!)

        return publishMediaPost(client, nativeId!!, imageUrl!!)?.apply {
            this.pageId = socialMedia.nativeId ?: -1
            this.socialMediaType = socialMedia.socialMediaType
        }
            ?: throw Exception("Something went wrong, could not publish a post.")
    }

    private fun publishMediaPost(client: FacebookClient, nativeId: Long, imageUrl: String): PostDto? {
        val containerId =
            client.publish("""$nativeId/media""", JsonObject::class.java, Parameter.with("image_url", imageUrl))
                .getString("id", null)
        val response = client.publish(
            """$nativeId/media_publish""",
            JsonObject::class.java,
            Parameter.with("creation_id", containerId)
        )
        return client.fetchObject(response.getString("id", null).toString(), PostDto::class.java)
    }

    override fun getAllConversations(socialMedia: SocialMedia): List<ConversationDto> {
        val client = getFacebookClient(socialMedia.token)

        val map: MultiValueMap<String, Any> = LinkedMultiValueMap()
        //TODO; move key to a secret place
        //TODO expiration timer
        map.add("access_token", socialMedia.token)

        val response =
            restTemplate.getForEntity(
                """https://graph.facebook.com/v13.0/debug_token?input_token=${socialMedia.token}&access_token=${socialMedia.token}""",
                TokenDebugResponseDto::class.java,
            )
        val pageId = response.body?.data?.profile_id

        val conversations = client.fetchConnection(
            """$pageId/conversations""",
            Conversation::class.java,
            Parameter.with("platform", "instagram"),
            Parameter.with("fields", "participants{username,user_id},message_count,can_reply,id,updated_time")
        ).data

        return conversations.map {
            conversationMapper.convertRestFbConversationToDto(it)
        }
    }

    override fun getConversationWithMessages(
        socialMedia: SocialMedia,
        conversationId: String
    ): ConversationWithMessagesDto {
        val client = getFacebookClient(socialMedia.token)

        val conversation = client.fetchObject(
            conversationId,
            Conversation::class.java,
            Parameter.with(
                "fields",
                "messages{message,attachments,from{username,user_id},created_time},message_count,can_reply"
            )
        )

        return conversationMapper.convertRestFbConversationToConversationWithMessagesDto(conversation)
            .also {
                val messageEntities = conversationMapper.convertRestFbConversationToMessages(it, socialMedia)
                eventPublisher.publishEvent(ConversationGetMessagesEvent(messageEntities))
            }
    }

    override fun sendMessageToConversation(
        socialMedia: SocialMedia,
        sendMessageDto: SendMessageDto,
        file: MultipartFile?
    ): MessageDto {
        val facebookClient = getFacebookClient(socialMedia.token)

        val response =
            restTemplate.getForEntity(
                """https://graph.facebook.com/v13.0/debug_token?input_token=${socialMedia.token}&access_token=${socialMedia.token}""",
                TokenDebugResponseDto::class.java,
            )
        val pageId = response.body?.data?.profile_id

        val recipient = IdMessageRecipient(sendMessageDto.recipientId)
        val testMessage = sendMessageDto.message?.let {
            val message = Message(it)
            val response = publishMessage(pageId!!.toLong(), facebookClient, recipient, message)
            val facebookMessage =
                fetchMessages(facebookClient, response.messageId, "message,created_time,from")
            return@let conversationMapper.convertRestFbMessageToMessageDto(facebookMessage)
        }

        val mediaMessage = sendMessageDto.attachment?.let {
            val imageUrl = saveImageService.saveImage(sendMessageDto.attachment!!)
            //todo: workout files properly
            val mediaAttachment = MediaAttachment(MediaAttachment.Type.IMAGE, imageUrl)
            val message = Message(mediaAttachment)
            val response =
                publishMessage(pageId!!.toLong(), facebookClient, recipient, message)
            val facebookMessage =
                fetchMessages(facebookClient, response.messageId, "attachments,created_time,from")
            return@let conversationMapper.convertRestFbMessageToMessageDto(facebookMessage)
        }

        return when {
            testMessage != null -> testMessage.apply {
                this.attachment = conversationMapper.convertMessageDtoToReceiveAttachment(mediaMessage)
            }
            mediaMessage != null -> mediaMessage.apply {
                this.nativeId = this.attachment?.nativeId!!
            }
            else -> throw Exception("No text or media was provided")
        }
    }

    fun getPostCommentsFromUnderAppToken(postId: String): CommentDto {
        var client = DefaultFacebookClient(Version.LATEST)
        val appAccessToken = client.obtainAppAccessToken(appId, appSecret)
        client = getFacebookClient(appAccessToken.accessToken)

        val comment = fetchComment(client, postId)
        return commentMapper.mapToCommentDto(comment)
    }

    private fun publishMessage(
        nativeId: Long,
        facebookClient: FacebookClient,
        recipient: IdMessageRecipient,
        message: Message
    ) = facebookClient.publish(
        """${nativeId}/messages""",
        SendResponse::class.java,
        Parameter.with("recipient", recipient),
        Parameter.with("message", message)
    )

    private fun fetchMessages(
        facebookClient: FacebookClient,
        messageId: String,
        fieldsString: String
    ) = facebookClient.fetchObject(
        messageId,
        com.restfb.types.Message::class.java,
        Parameter.with("fields", fieldsString)
    )

    override fun getPostComments(socialMedia: SocialMedia, postId: String): List<CommentDto> {
        val client = getFacebookClient(socialMedia.token)

        return fetchComments(client, postId).data.map { commentMapper.mapToCommentDto(it) }
    }

    private fun fetchComments(client: FacebookClient, postId: String) =
        client.fetchConnection(
            """$postId/comments""",
            JsonObject::class.java,
            Parameter.with("fields", "from,id,like_count,media{media_type,media_url},parent_id,text,timestamp,username")
        )

    override fun publishComment(socialMedia: SocialMedia, postId: String, commentDto: PublishCommentDto): CommentDto {
        TODO("not supported by facebook, see https://developers.facebook.com/docs/instagram-api/reference/ig-comment#creating")
    }

    override fun respondToComment(
        socialMedia: SocialMedia,
        commentId: String,
        commentDto: PublishCommentDto
    ): CommentDto = doFacebookClientAction(socialMedia.token, commentId) { client, id ->
        val response = publishReply(client, commentId, commentDto) ?: throw Exception("failed to send a response")
        val temp = fetchComment(client, response.getString("id", null))
        commentMapper.mapToCommentDto(temp)
    }

    private fun publishReply(client: FacebookClient, commentId: String, commentDto: PublishCommentDto) = client.publish(
        """$commentId/replies""",
        JsonObject::class.java,
        Parameter.with("message", commentDto.content)
    )

    private fun fetchComment(client: FacebookClient, commentId: String): JsonObject = client.fetchObject(
        commentId,
        JsonObject::class.java,
        Parameter.with("fields", "from,id,like_count,media{media_type,id},parent_id,text,timestamp,username")
    )
}