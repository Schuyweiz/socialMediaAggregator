package com.example.api.service

import com.example.api.config.PostServiceRegistry
import com.example.core.dto.PostDto
import com.example.core.dto.PublishPostDto
import com.example.core.model.SocialMedia
import com.example.core.model.User
import com.example.core.service.impl.UserQueryService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PostService(
    private val uerQueryService: UserQueryService,
    private val postServiceRegistry: PostServiceRegistry
) {

    @Transactional
    fun getAllUserPosts(user: User): List<PostDto> {
        val user = uerQueryService.findByIdOrThrow(user.id)

        return getPosts(user.socialMediaSet)
    }

    @Transactional
    fun publishPostAllAccounts(userId: Long, postDto: PublishPostDto): List<PostDto?> {
        val user = uerQueryService.findByIdOrThrow(userId)

        return publishPostAllAccounts(user.socialMediaSet, postDto)
    }

    @Transactional
    fun publishPost(userId: Long, postDto: PublishPostDto, socialMediaId: Long): PostDto {
        val user = uerQueryService.findByIdOrThrow(userId)

        return user.socialMediaSet.singleOrNull { it.id == socialMediaId }?.let {
            postServiceRegistry.getPostService(it.socialMediaType.getApiService()).publishPost(it, postDto)
        } ?: throw Exception("Could not find a social media account with an id $socialMediaId")
    }

    @Transactional
    fun publishPost(userId: Long, postDto: PublishPostDto, socialMediaIds: Set<Long>): List<PostDto?> {
        val user = uerQueryService.findByIdOrThrow(userId)

        return user.socialMediaSet.filter { socialMediaIds.contains(it.id) }
            .map { postServiceRegistry.getPostService(it.socialMediaType.getApiService()).publishPost(it, postDto) }
            .ifEmpty {
                throw Exception("No such social media exist as $socialMediaIds")
            }
    }

    private fun publishPostAllAccounts(tokens: Set<SocialMedia>, post: PublishPostDto): List<PostDto?> =
        tokens.map {
            postServiceRegistry.getPostService(it.socialMediaType.getApiService())
                .publishPost(socialMedia = it, postDto = post)
        }

    private fun getPosts(tokens: Set<SocialMedia>): List<PostDto> =
        tokens.flatMap {
            postServiceRegistry.getPostService(it.socialMediaType.getApiService())
                .getPosts(it) ?: emptyList()
        }
}