package com.example.api.service.impl

import com.example.api.service.SocialMediaPosting
import com.example.core.model.SocialMedia
import com.example.core.dto.PostDto
import com.example.core.dto.PublishPostDto
import com.example.core.annotation.Logger
import com.restfb.DefaultFacebookClient
import com.restfb.Parameter
import com.restfb.Version
import org.springframework.stereotype.Service

@Service
@Logger
class FacebookUserApiService(
) : SocialMediaPosting {
    override fun getPosts(socialMedia: SocialMedia): List<PostDto> {
        val facebookClient = getFacebookClient(socialMedia.token)
        val posts =
            facebookClient.fetchConnection("me/feed", PostDto::class.java, Parameter.with("fields", "id, message"))

        return posts.data
    }

    override fun publishPost(socialMedia: SocialMedia, postDto: PublishPostDto): List<PostDto> {
        TODO("Not yet implemented")
    }

    private fun getFacebookClient(token: String) = DefaultFacebookClient(token, Version.LATEST)
}