package com.example.api.service.impl.facebook

import com.example.api.dto.*
import com.example.api.dto.message.MessageDto
import com.example.api.dto.message.SendMessageDto
import com.example.api.service.SocialMediaConversation
import com.example.api.service.SocialMediaPosting
import com.example.core.annotation.Logger
import com.example.core.dto.PostDto
import com.example.core.dto.PublishPostDto
import com.example.core.model.SocialMedia
import com.example.core.model.socialmedia.SocialMediaType
import com.restfb.DefaultFacebookClient
import com.restfb.Parameter
import com.restfb.Version
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.Instant
import java.util.*

@Service
@Logger
class FacebookUserApiService(
) : SocialMediaPosting, SocialMediaConversation {
    override fun getPosts(socialMedia: SocialMedia): List<PostDto> {
        val facebookClient = getFacebookClient(socialMedia.token)
        val posts =
            facebookClient.fetchConnection("me/feed", PostDto::class.java, Parameter.with("fields", "id, message"))

        return posts.data
    }

    override fun publishPost(socialMedia: SocialMedia, postDto: PublishPostDto): PostDto {
        //todo: implement when facebook allows it
        return PostDto("", "", 0L, 0L, socialMediaType = SocialMediaType.FACEBOOK_USER)
    }

    private fun getFacebookClient(token: String) = DefaultFacebookClient(token, Version.LATEST)
    override fun getAllConversations(socialMedia: SocialMedia): List<ConversationDto> {
        //todo: implement when facebook allows it
        return emptyList()
    }

    override fun getConversationWithMessages(
        socialMedia: SocialMedia,
        conversationId: String
    ): ConversationWithMessagesDto {
        //todo: implement when can do
        return ConversationWithMessagesDto(
            conversationDto = ConversationDto(
                conversationId = "",
                updatedTime = Date.from(Instant.now()),
                messageCount = 0,
                unreadCount = 0,
                participants = listOf(),
                canReply = false,
                createdTime = null,
            ), messagesDto = listOf()
        )
    }

    override fun sendMessageToConversation(
        socialMedia: SocialMedia,
        sendMessageDto: SendMessageDto,
        file: MultipartFile?
    ): MessageDto {
        return MessageDto(
            nativeId = "",
            message = null,
            participantDto = ParticipantDto(nativeId = "", name = ""),
            createdTime = Date.from(Instant.now()),
            attachment = null
        )
    }
}