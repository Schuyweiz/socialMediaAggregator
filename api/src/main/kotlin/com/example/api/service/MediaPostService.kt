package com.example.api.service

import com.example.api.config.PostServiceRegistry
import com.example.core.model.SocialMediaToken
import com.example.core.model.User
import com.example.core.model.socialmedia.Post
import com.example.core.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MediaPostService(
    private val userRepository: UserRepository,
    private val postServiceRegistry: PostServiceRegistry
) {

    @Transactional
    fun getAllUserPosts(user: User): List<Post>{
        val user = userRepository.findByIdOrElseThrow(user.id)

        return getPosts(user.socialMediaTokens)
    }

    private fun getPosts(tokens: Set<SocialMediaToken>): List<Post> =
        tokens.flatMap {
        postServiceRegistry.getPostService(it.socialMediaType.getApiService())
            .getPosts(it)
    }
}