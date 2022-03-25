package com.example.auth.socialmedia

import com.example.core.model.socialmedia.SocialMediaDto
import com.example.core.user.repository.SocialMediaRepository
import com.example.core.utils.Logger
import com.example.core.utils.SocialMediaMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Logger
class UserSocialMediaService(
    private val mapper: SocialMediaMapper,
    private val socialMediaRepository: SocialMediaRepository,
) {

    @Transactional(readOnly = true)
    fun getAuthenticatedSocialMedia(userId: Long): List<SocialMediaDto> =
        socialMediaRepository.findAllByUserId(userId).map {
            mapper.mapToDto(it)
        }
}