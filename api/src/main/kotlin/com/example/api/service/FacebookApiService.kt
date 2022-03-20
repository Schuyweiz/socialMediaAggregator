package com.example.api.service

import com.example.core.model.SocialMediaToken
import com.example.core.model.socialmedia.Post
import com.example.core.utils.Logger
import com.restfb.DefaultFacebookClient
import com.restfb.Version
import org.springframework.stereotype.Service

@Service
@Logger
class FacebookApiService(
) : SocialMediaPosting {
    override fun getPosts(token: SocialMediaToken): List<Post> {
        val facebookClient = getFacebookClient(token.token)

        val posts = facebookClient.fetchConnection("me/feed", Post::class.java)

        return posts.data
    }

    private fun getFacebookClient(token: String) = DefaultFacebookClient(token, Version.LATEST)
}