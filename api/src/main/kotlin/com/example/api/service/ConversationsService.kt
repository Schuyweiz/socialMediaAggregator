package com.example.api.service

import com.example.api.config.ConversationServiceRegistry
import com.example.api.dto.ConversationDto
import com.example.core.model.SocialMedia
import com.example.core.repository.SocialMediaRepository
import com.example.core.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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

    private fun getConversations(socialMedia: SocialMedia): List<ConversationDto> {
        val apiServiceBean = socialMedia.socialMediaType.getApiService()
        return conversationRegistry.getConversationService(apiServiceBean).getAllConversations(socialMedia)
    }
}