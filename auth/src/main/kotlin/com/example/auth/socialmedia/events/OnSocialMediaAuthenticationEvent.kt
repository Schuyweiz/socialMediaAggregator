package com.example.auth.socialmedia.events

import com.restfb.FacebookClient.AccessToken
import org.springframework.context.ApplicationEvent


class OnSocialMediaAuthenticationEvent(token: AccessToken) : ApplicationEvent(token) {
}