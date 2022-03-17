package com.example.auth.api.service

import com.example.auth.api.config.PostServiceRegistry
import com.example.core.user.model.SocialMediaToken
import com.example.core.user.model.User
import com.example.core.user.model.socialmedia.Post
import com.example.core.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class MediaPostService(
    private val userRepository: UserRepository,
    private val postServiceRegistry: PostServiceRegistry
) {

    fun getAllUserPosts(user: User): List<Post>{
        val user = userRepository.findByIdOrElseThrow(user)

        return getPosts(user.socialMediaTokens)
    }

    private fun getPosts(tokens: Set<SocialMediaToken>): List<Post> =
        tokens.flatMap {
        postServiceRegistry.getPostService(it.socialMediaType.getApiService())
            .getPosts(it)
    }
}