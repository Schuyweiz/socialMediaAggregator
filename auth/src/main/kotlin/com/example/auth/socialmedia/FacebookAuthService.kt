package com.example.auth.socialmedia

import com.example.core.config.FacebookConfiguration
import com.example.core.model.SocialMedia
import com.example.core.model.SocialMediaType.*
import com.example.core.model.User
import com.example.core.model.socialmedia.FacebookPage
import com.example.core.user.repository.SocialMediaRepository
import com.example.core.user.repository.UserRepository
import com.example.core.utils.Logger
import com.restfb.DefaultFacebookClient
import com.restfb.FacebookClient
import com.restfb.Parameter
import com.restfb.Version
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Logger
class FacebookAuthService(
    private val facebookClient: DefaultFacebookClient,
    private val userRepository: UserRepository,
    private val socialMediaRepository: SocialMediaRepository,
    @Value("\${app.facebook.app-secret}")
    private val appSecret: String,
    @Value("\${app.facebook.app-id}")
    private val appId: String,
    @Value("\${app.facebook.auth.redirect-url}")
    private val redirectUrl: String,
) {

    @Transactional
    fun authenticateUser(verificationCode: String, user: User) =
        with(facebookClient.obtainUserAccessToken(appId, appSecret, redirectUrl,
            verificationCode)) {
            userRepository.findById(user.id)
            val extendedToken = getExtendedAccessToken(this.accessToken).accessToken
            return@with socialMediaRepository.save(SocialMedia(token = extendedToken, socialMediaType = FACEBOOK_USER, user = user, nativeId = null))
        }

    fun getLoginDialogueUrl(): String = facebookClient.getLoginDialogUrl(appId, redirectUrl, FacebookConfiguration.scope)

    private fun getExtendedAccessToken(token: String): FacebookClient.AccessToken = facebookClient.obtainExtendedAccessToken(appId, appSecret, token)

    fun getUserPages(userId: Long): List<FacebookPage> {
        val user = userRepository.findByIdWithSocialMediaOrThrow(userId)
        return getPages(user.socialMediaSet)
    }

    @Transactional
    fun authenticateUserPage(userId: Long, pageId: Long): SocialMedia {
        val user = userRepository.findByIdWithSocialMediaOrThrow(userId)

        getPages(user.socialMediaSet)
            .singleOrNull {
            it.pageId == pageId
        }?.let {
                return socialMediaRepository.save(SocialMedia(nativeId = it.pageId, token = it.accessToken, socialMediaType = FACEBOOK_PAGE, user = user))
            }?: kotlin.run {
                throw IllegalArgumentException("No bijection between facebook page and page id.")
        }
    }

    private fun getUserClient(token: String) = DefaultFacebookClient(token, Version.LATEST)

    private fun getPages(userSocialMedia: Set<SocialMedia>) = userSocialMedia
        .filter { it.socialMediaType == FACEBOOK_USER }
        .flatMap { fetchPagesConnection(it.token) }
        .flatten()

    private fun fetchPagesConnection(userToken: String) = getUserClient(userToken).fetchConnection(
        "/me/accounts", FacebookPage::class.java, Parameter.with("fields","id,name,access_token"))
}