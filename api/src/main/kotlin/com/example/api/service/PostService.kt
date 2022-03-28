package com.example.api.service

import com.example.api.config.PostServiceRegistry
import com.example.core.dto.PostDto
import com.example.core.dto.PublishPostDto
import com.example.core.model.SocialMedia
import com.example.core.model.User
import com.example.core.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService(
    private val userRepository: UserRepository,
    private val postServiceRegistry: PostServiceRegistry
) {

    @Transactional
    fun getAllUserPosts(user: User): List<PostDto> {
        val user = userRepository.findByIdOrElseThrow(user.id)

        return getPosts(user.socialMediaSet)
    }

    @Transactional
    fun publishPost(userId: Long, postDto: PublishPostDto): List<PostDto> {
        val user = userRepository.findByIdOrElseThrow(userId)

        return publishPost(user.socialMediaSet, postDto)
    }

    private fun publishPost(tokens: Set<SocialMedia>, post: PublishPostDto): List<PostDto> =
        tokens.map {
            postServiceRegistry.getPostService(it.socialMediaType.getApiService())
                .publishPost(socialMedia = it, postDto = post)
        }

    private fun getPosts(tokens: Set<SocialMedia>): List<PostDto> =
        tokens.flatMap {
            postServiceRegistry.getPostService(it.socialMediaType.getApiService())
                .getPosts(it)
        }
}