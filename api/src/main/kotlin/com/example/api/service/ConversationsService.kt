package com.example.api.service

import com.example.api.config.ConversationServiceRegistry
import com.example.api.dto.ConversationDto
import com.example.api.dto.ConversationWithMessagesDto
import com.example.api.dto.MessageDto
import com.example.api.dto.SendMessageDto
import com.example.core.model.SocialMedia
import com.example.core.repository.SocialMediaRepository
import com.example.core.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class ConversationsService(
    private val userRepository: UserRepository,
    private val socialMediaRepository: SocialMediaRepository,
    private val conversationRegistry: ConversationServiceRegistry,
) {

    @Transactional(readOnly = true)
    fun getAllUserConversations(userId: Long): List<List<ConversationDto>> {
        val user = userRepository.findByIdOrElseThrow(userId)

        return user.socialMediaSet.map {
            getConversations(it)
        }
    }

    @Transactional(readOnly = true)
    fun getAllConversationsBySocialMediaId(socialMediaId: Long): List<ConversationDto> {
        socialMediaRepository.findByIdOrThrow(socialMediaId).run {
            return getConversations(this)
        }
    }

    @Transactional
    fun getConversationWithMessages(
        socialMediaId: Long,
        conversationId: String
    ): ConversationWithMessagesDto {
        val socialMedia = socialMediaRepository.findByIdOrThrow(socialMediaId)
        val apiServiceBean = socialMedia.socialMediaType.getApiService()

        return conversationRegistry.getConversationService(apiServiceBean)
            .getConversationWithMessages(socialMedia, conversationId)
    }

    @Transactional
    fun sendMessage(socialMediaId: Long, conversationId: String, sendMessageDto: SendMessageDto): MessageDto {
        val socialMedia = socialMediaRepository.findByIdOrThrow(socialMediaId)
        val apiServiceBean = socialMedia.socialMediaType.getApiService()

        return conversationRegistry.getConversationService(apiServiceBean)
            .sendMessageToConversation(socialMedia, conversationId, sendMessageDto, sendMessageDto.attachment)
    }

    private fun getConversations(socialMedia: SocialMedia): List<ConversationDto> {
        val apiServiceBean = socialMedia.socialMediaType.getApiService()
        return conversationRegistry.getConversationService(apiServiceBean).getAllConversations(socialMedia)
    }
}