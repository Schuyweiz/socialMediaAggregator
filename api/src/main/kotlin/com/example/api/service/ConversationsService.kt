package com.example.api.service

import com.example.api.config.ConversationServiceRegistry
import com.example.api.dto.ConversationDto
import com.example.api.dto.ConversationWithMessagesDto
import com.example.api.dto.message.MessageDto
import com.example.api.dto.message.SendMessageDto
import com.example.core.model.SocialMedia
import com.example.core.repository.UserRepository
import com.example.core.service.impl.SocialMediaQueryService
import com.example.core.service.impl.UserQueryService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ConversationsService(
    private val userRepository: UserRepository,
    private val userQueryService: UserQueryService,
    private val socialMediaQueryService: SocialMediaQueryService,
    private val conversationRegistry: ConversationServiceRegistry,
) {

    @Transactional(readOnly = true)
    fun getAllUserConversations(userId: Long): List<List<ConversationDto>> {
        val user = userQueryService.findByIdOrThrow(userId)

        return user.socialMediaSet.map {
            getConversations(it)
        }
    }

    @Transactional(readOnly = true)
    fun getAllConversationsBySocialMediaId(socialMediaId: Long): List<ConversationDto> {
        socialMediaQueryService.findByIdOrThrow(socialMediaId).run {
            return getConversations(this)
        }
    }

    @Transactional
    fun getConversationWithMessages(
        socialMediaId: Long,
        conversationId: String
    ): ConversationWithMessagesDto {
        val socialMedia = socialMediaQueryService.findByIdOrThrow(socialMediaId)
        val apiServiceBean = socialMedia.socialMediaType.getApiService()

        return conversationRegistry.getConversationService(apiServiceBean)
            .getConversationWithMessages(socialMedia, conversationId)
    }

    @Transactional
    fun sendMessage(socialMediaId: Long, sendMessageDto: SendMessageDto): MessageDto {
        val socialMedia = socialMediaQueryService.findByIdOrThrow(socialMediaId)
        val apiServiceBean = socialMedia.socialMediaType.getApiService()

        return conversationRegistry.getConversationService(apiServiceBean)
            .sendMessageToConversation(socialMedia, sendMessageDto, sendMessageDto.attachment)
    }

    private fun getConversations(socialMedia: SocialMedia): List<ConversationDto> {
        val apiServiceBean = socialMedia.socialMediaType.getApiService()
        return conversationRegistry.getConversationService(apiServiceBean).getAllConversations(socialMedia)
    }
}