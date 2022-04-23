package com.example.core.service

import com.restfb.DefaultFacebookClient
import com.restfb.Facebook
import com.restfb.FacebookClient
import com.restfb.Version

interface FacebookApi {
    fun getFacebookClient(token: String) = DefaultFacebookClient(token, Version.LATEST)

    fun <T> doFacebookClientAction(
        token: String,
        fbEntityId: String,
        action: (FacebookClient, String) -> T
    ): T = action.invoke(getFacebookClient(token), fbEntityId)
}