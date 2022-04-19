package com.example.core.service

import com.restfb.DefaultFacebookClient
import com.restfb.Version

interface FacebookApi {
    fun getFacebookClient(token: String) = DefaultFacebookClient(token, Version.LATEST)
}