package com.example.auth.socialmedia

import com.example.auth.socialmedia.events.OnSocialMediaAuthenticationEvent
import com.restfb.DefaultFacebookClient
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class FacebookAuthService(
    private val facebookClient: DefaultFacebookClient,
    private val applicationEventPublisher: ApplicationEventPublisher,
) {

    //todo: вынести контсанты из кода
    fun authenticateUser(verificationCode: String) =
        with(facebookClient.obtainUserAccessToken("1005503330221024",
            "08953917c4e63dcd7e145dd5b208071c",
            "https://localhost:8443/auth/facebook", verificationCode)) {
            applicationEventPublisher.publishEvent(OnSocialMediaAuthenticationEvent(this))
        }

}