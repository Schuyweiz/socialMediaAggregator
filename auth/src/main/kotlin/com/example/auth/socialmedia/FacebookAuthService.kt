package com.example.auth.socialmedia

import com.example.auth.socialmedia.events.OnFacebookPageAuthenticationEvent
import com.example.auth.socialmedia.events.OnSocialMediaAuthenticationEvent
import com.example.core.config.FacebookConfiguration
import com.example.core.model.SocialMediaToken
import com.example.core.model.SocialMediaType
import com.example.core.model.User
import com.example.core.model.socialmedia.FacebookPage
import com.example.core.user.repository.UserRepository
import com.restfb.DefaultFacebookClient
import com.restfb.FacebookClient
import com.restfb.Parameter
import com.restfb.Version
import com.restfb.types.Page
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import java.util.Objects

@Service
class FacebookAuthService(
    private val facebookClient: DefaultFacebookClient,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val userRepository: UserRepository,
    @Value("\${app.facebook.app-secret}")
    private val appSecret: String,
    @Value("\${app.facebook.app-id}")
    private val appId: String,
    @Value("\${app.facebook.auth.redirect-url}")
    private val redirectUrl: String,
) {

    fun authenticateUser(verificationCode: String, user: User) =
        with(facebookClient.obtainUserAccessToken(appId, appSecret, redirectUrl,
            verificationCode)) {
            applicationEventPublisher.publishEvent(OnSocialMediaAuthenticationEvent(this, user))
        }

    fun getLoginDialogueUrl(): String = facebookClient.getLoginDialogUrl(appId, redirectUrl, FacebookConfiguration.scope)

    fun getExtendedAccessToken(token: String): FacebookClient.AccessToken = facebookClient.obtainExtendedAccessToken(appId, appSecret, token)

    fun getUserPages(userId: Long): List<FacebookPage> {
        val user = userRepository.findByIdWithTokensOrThrow(userId)
        return getPages(user)
    }

    fun authenticateUserPage(userId: Long, pageId: Long){
        val user = userRepository.findByIdWithTokensOrThrow(userId)

        getFacebookPageToSocialToken(user.socialMediaTokens)
            .filter { it.key.pageId == pageId }
            .entries
            .firstOrNull()?.let {
                applicationEventPublisher.publishEvent(OnFacebookPageAuthenticationEvent(user, it.value, it.key.accessToken))
            }?: run{
                throw IllegalArgumentException("""Page with id $pageId does not match bijectively into user ${user.id} social media token """)
        }

    }

    private fun getUserClient(token: String) = DefaultFacebookClient(token, Version.LATEST)

    private fun getPages(user: User) = user.socialMediaTokens
        .filter { it.socialMediaType == SocialMediaType.FACEBOOK }
        .flatMap { fetchPagesConnection(it.token) }
        .flatten()

    private fun getFacebookPageToSocialToken(socialMediaTokens: Set<SocialMediaToken>) = socialMediaTokens
        .filter { it.isFacebook() }
        .map {
            fetchPagesConnection(it.token).flatten() to it
        }.flatMap{
            it.first.map { first-> first to it.second }
        }.toMap()

    private fun fetchPagesConnection(userToken: String) = getUserClient(userToken).fetchConnection(
        "/me/accounts", FacebookPage::class.java, Parameter.with("fields","id,name,access_token"))
}