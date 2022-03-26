package com.example.auth.socialmedia.service

import com.example.core.dto.SocialMediaDto
import com.example.core.repository.SocialMediaRepository
import com.example.core.annotation.Logger
import com.example.core.mapper.SocialMediaMapper
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