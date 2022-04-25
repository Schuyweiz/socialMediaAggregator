package com.example.auth.socialmedia.service

import com.example.core.annotation.Logger
import com.example.core.dto.PageAuthenticateDto
import com.example.core.dto.SocialMediaDto
import com.example.core.mapper.SocialMediaMapper
import com.example.core.model.SocialMedia
import com.example.core.model.SocialMediaType
import com.example.core.model.SocialMediaType.*
import com.example.core.model.User
import com.example.core.repository.SocialMediaRepository
import com.example.core.repository.UserRepository
import com.example.core.service.impl.UserQueryService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Logger
class FacebookAuthService(
    private val facebookBasicAuthService: FacebookBasicAuthService,
    private val userRepository: UserRepository,
    private val userQueryService: UserQueryService,
    private val socialMediaRepository: SocialMediaRepository,
    private val socialMediaMapper: SocialMediaMapper,
) {

    fun getUserPages(userId: Long): List<PageAuthenticateDto> {
        val user = userQueryService.findByIdOrThrow(userId)
        return getUserPagesAuthenticateDto(user.socialMediaSet)
    }

    @Transactional
    fun authenticateUserPage(userId: Long, pageId: Long): SocialMediaDto {
        val user = userQueryService.findByIdOrThrow(userId)

        getUserPages(user.socialMediaSet)
            .singleOrNull {
                it.id == pageId
            }?.let {
                return saveSocialMedia(it, user, FACEBOOK_PAGE, it.id)
            } ?: kotlin.run {
            throw IllegalArgumentException("No bijection between facebook page and page id $pageId")
        }
    }

    @Transactional
    fun authenticateInstagramPage(userId: Long, accountId: Long): SocialMediaDto {
        val user = userQueryService.findByIdOrThrow(userId)

        getUserPages(user.socialMediaSet)
            .singleOrNull {
                it.instagramId == accountId
            }?.let {
                return saveSocialMedia(it, user, INSTAGRAM, it.instagramId!!)
            } ?: kotlin.run {
            throw IllegalArgumentException("No bijection between facebook page and instagram accountId $accountId.")
        }
    }

    fun getInstagramPages(userId: Long) = getUserPages(userId).filter { it.instagramId != null }

    private fun getUserPagesAuthenticateDto(userSocialMedia: Set<SocialMedia>) =
        facebookBasicAuthService.getPagesOfType(userSocialMedia, FACEBOOK_USER)

    private fun getUserPages(userSocialMedia: Set<SocialMedia>) =
        facebookBasicAuthService.getPagesOfType(userSocialMedia, socialMediatype = FACEBOOK_USER)

    private fun saveSocialMedia(
        dto: PageAuthenticateDto,
        user: User,
        socialMediaType: SocialMediaType,
        nativeId: Long,
    ): SocialMediaDto {
        val socialMedia = socialMediaRepository.findByNativeIdAndSocialMediaType(nativeId, socialMediaType)?.apply {
            this.token = dto.token
        } ?: SocialMedia(
            nativeId = nativeId,
            token = dto.token,
            socialMediaType = socialMediaType,
            user = user
        )
        val savedSocialMedia = socialMediaRepository.save(socialMedia)
        return socialMediaMapper.mapToDto(savedSocialMedia)
    }

}