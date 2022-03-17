package com.example.auth.socialmedia

import com.example.auth.socialmedia.events.OnSocialMediaAuthenticationEvent
import com.example.core.config.FacebookConfiguration
import com.example.core.user.model.User
import com.restfb.DefaultFacebookClient
import com.restfb.FacebookClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class FacebookAuthService(
    private val facebookClient: DefaultFacebookClient,
    private val applicationEventPublisher: ApplicationEventPublisher,
    @Value("app.facebook.app-secret")
    private val appSecret: String,
    @Value("app.facebook.app-id")
    private val appId: String,
    @Value("app.facebook.auth.redirect-url")
    private val redirectUrl: String,
) {

    fun authenticateUser(verificationCode: String, user: User) =
        with(facebookClient.obtainUserAccessToken(appId, appSecret, redirectUrl,
            verificationCode)) {
            applicationEventPublisher.publishEvent(OnSocialMediaAuthenticationEvent(this, user))
        }

    fun getLoginDialogueUrl(): String = facebookClient.getLoginDialogUrl(appId, redirectUrl, FacebookConfiguration.scope)

    fun getExtendedAccessToken(token: String): FacebookClient.AccessToken = facebookClient.obtainExtendedAccessToken(appId, appSecret, token)
}