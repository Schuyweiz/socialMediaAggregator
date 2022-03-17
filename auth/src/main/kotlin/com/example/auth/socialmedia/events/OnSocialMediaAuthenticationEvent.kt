package com.example.auth.socialmedia.events

import com.example.core.user.model.User
import com.restfb.FacebookClient.AccessToken
import org.springframework.context.ApplicationEvent


class OnSocialMediaAuthenticationEvent(val token: AccessToken,val user: User) : ApplicationEvent(user) {
}