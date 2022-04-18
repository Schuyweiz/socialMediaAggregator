package com.example.auth.socialmedia.service

import com.example.core.config.FacebookConfiguration
import com.example.core.model.SocialMedia
import com.example.core.model.SocialMediaType
import com.example.core.model.SocialMediaType.*
import com.example.core.model.User
import com.example.core.dto.PageAuthenticateDto
import com.example.core.dto.SocialMediaDto
import com.example.core.repository.SocialMediaRepository
import com.example.core.repository.UserRepository
import com.example.core.annotation.Logger
import com.example.core.mapper.SocialMediaMapper
import com.restfb.DefaultFacebookClient
import com.restfb.FacebookClient
import com.restfb.Parameter
import com.restfb.Version
import com.restfb.types.Page
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Logger
class FacebookAuthService(
    private val facebookClient: DefaultFacebookClient,
    private val userRepository: UserRepository,
    private val socialMediaRepository: SocialMediaRepository,
    private val socialMediaMapper: SocialMediaMapper,
    @Value("\${app.facebook.app-secret}")
    private val appSecret: String,
    @Value("\${app.facebook.app-id}")
    private val appId: String,
    @Value("\${app.facebook.auth.redirect-url}")
    private val redirectUrl: String,
) {

    @Transactional
    fun authenticateUser(verificationCode: String, userId: Long): SocialMediaDto =
        with(
            facebookClient.obtainUserAccessToken(
                appId, appSecret, redirectUrl,
                verificationCode
            )
        ) {
            val user = userRepository.findByIdOrElseThrow(userId)
            val extendedToken = getExtendedAccessToken(this.accessToken).accessToken
            val socialMedia = socialMediaRepository.save(
                SocialMedia(
                    token = extendedToken,
                    socialMediaType = FACEBOOK_USER,
                    user = user,
                    nativeId = null
                )
            )
            return@with socialMediaMapper.mapToDto(socialMedia)
        }

    fun getLoginDialogueUrl(): String =
        facebookClient.getLoginDialogUrl(appId, redirectUrl, FacebookConfiguration.scope)

    fun getUserPages(userId: Long): List<PageAuthenticateDto> {
        val user = userRepository.findByIdWithSocialMediaOrThrow(userId)
        return getUserPagesAuthenticateDto(user.socialMediaSet)
    }

    @Transactional
    fun authenticateUserPage(userId: Long, pageId: Long): SocialMediaDto {
        val user = userRepository.findByIdWithSocialMediaOrThrow(userId)

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
        val user = userRepository.findByIdOrElseThrow(userId)

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
        getPagesOfType(userSocialMedia, FACEBOOK_USER)

    private fun getExtendedAccessToken(token: String): FacebookClient.AccessToken =
        facebookClient.obtainExtendedAccessToken(appId, appSecret, token)

    private fun getUserClient(token: String) = DefaultFacebookClient(token, Version.LATEST)


    private fun getPagesOfType(userSocialMedia: Set<SocialMedia>, socialMediatype: SocialMediaType) = userSocialMedia
        .filter { it.socialMediaType == socialMediatype }
        .flatMap { fetchPagesConnection(it.token) }
        .flatten()
        .map { socialMediaMapper.mapAuthenticationDto(it) }

    private fun getUserPages(userSocialMedia: Set<SocialMedia>) =
        getPagesOfType(userSocialMedia, socialMediatype = FACEBOOK_USER)

    private fun getPagePages(userSocialMedia: Set<SocialMedia>) =
        getPagesOfType(userSocialMedia, socialMediatype = FACEBOOK_PAGE)

    private fun fetchPagesConnection(userToken: String) = getUserClient(userToken).fetchConnection(
        "/me/accounts",
        Page::class.java,
        Parameter.with("fields", "id,name,access_token,instagram_business_account")
    )

    private fun saveSocialMedia(
        dto: PageAuthenticateDto,
        user: User,
        socialMediaType: SocialMediaType,
        nativeId: Long,
    ): SocialMediaDto {
        val socialMedia = socialMediaRepository.findByNativeIdAndSocialMediaType(nativeId, FACEBOOK_PAGE)?.apply {
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