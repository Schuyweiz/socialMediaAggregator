package com.example.auth.socialmedia.service

import com.example.core.annotation.Logger
import com.example.core.config.FacebookConfiguration
import com.example.core.dto.SocialMediaDto
import com.example.core.mapper.SocialMediaMapper
import com.example.core.model.SocialMedia
import com.example.core.model.socialmedia.SocialMediaType
import com.example.core.repository.SocialMediaRepository
import com.example.core.repository.UserRepository
import com.example.core.service.FacebookApi
import com.example.core.service.impl.UserQueryService
import com.restfb.DefaultFacebookClient
import com.restfb.FacebookClient
import com.restfb.Parameter
import com.restfb.types.Page
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Logger
@Service
class FacebookBasicAuthService(
    private val facebookClient: DefaultFacebookClient,
    private val userRepository: UserRepository,
    private val userQueryService: UserQueryService,
    private val socialMediaRepository: SocialMediaRepository,
    private val socialMediaMapper: SocialMediaMapper,
    @Value("\${app.facebook.app-secret}")
    private val appSecret: String,
    @Value("\${app.facebook.app-id}")
    private val appId: String,
    @Value("\${app.facebook.auth.redirect-url}")
    private val redirectUrl: String,
) : FacebookApi {
    @Transactional
    fun authenticateUser(verificationCode: String, userId: Long): SocialMediaDto =
        with(
            facebookClient.obtainUserAccessToken(
                appId, appSecret, redirectUrl,
                verificationCode
            )
        ) {
            val user = userQueryService.findByIdOrThrow(userId)
            val extendedToken = getExtendedAccessToken(this.accessToken).accessToken
            val socialMedia = socialMediaRepository.save(
                SocialMedia(
                    token = extendedToken,
                    socialMediaType = SocialMediaType.FACEBOOK_USER,
                    user = user,
                    nativeId = null
                )
            )
            return@with socialMediaMapper.mapToDto(socialMedia)
        }

    fun getPagesOfType(userSocialMedia: Set<SocialMedia>, socialMediatype: SocialMediaType) = userSocialMedia
        .filter { it.socialMediaType == socialMediatype }
        .flatMap { fetchPagesConnection(it.token) }
        .flatten()
        .map { socialMediaMapper.mapAuthenticationDto(it) }

    private fun fetchPagesConnection(userToken: String) = getFacebookClient(userToken).fetchConnection(
        "/me/accounts",
        Page::class.java,
        Parameter.with("fields", "id,name,access_token,instagram_business_account")
    )

    fun getLoginDialogueUrl(): String =
        facebookClient.getLoginDialogUrl(appId, redirectUrl, FacebookConfiguration.scope)

    private fun getExtendedAccessToken(token: String): FacebookClient.AccessToken =
        facebookClient.obtainExtendedAccessToken(appId, appSecret, token)

}