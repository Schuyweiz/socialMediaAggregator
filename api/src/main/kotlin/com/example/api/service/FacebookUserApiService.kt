package com.example.api.service

import com.example.core.model.SocialMedia
import com.example.core.model.socialmedia.Post
import com.example.core.utils.Logger
import com.restfb.DefaultFacebookClient
import com.restfb.Parameter
import com.restfb.Version
import com.restfb.types.User
import com.restfb.types.Post as fbPost
import org.springframework.stereotype.Service

@Service
@Logger
class FacebookUserApiService(
) : SocialMediaPosting {
    override fun getPosts(token: SocialMedia): List<Post> {
        val facebookClient = getFacebookClient(token.token)
        val posts = facebookClient.fetchConnection("me/feed", fbPost::class.java, Parameter.with("fields", "id, message"))
        val user = facebookClient.fetchObject("me", User::class.java)

        return posts.flatten()
            .map { Post(socialMediaId = it.id.toLong(), textContent = it.message) }
    }

    private fun getFacebookClient(token: String) = DefaultFacebookClient(token, Version.LATEST)
}