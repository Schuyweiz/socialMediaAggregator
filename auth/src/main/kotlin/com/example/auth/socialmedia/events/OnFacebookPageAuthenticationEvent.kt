package com.example.auth.socialmedia.events

import com.example.core.model.SocialMediaToken
import com.example.core.model.User
import org.springframework.context.ApplicationEvent

class OnFacebookPageAuthenticationEvent(
    val user: User,
    val userToken: SocialMediaToken,
    val pageToken: String
): ApplicationEvent(user) {
}