package com.example.api.service

import com.example.api.config.PostServiceRegistry
import com.example.core.dto.PostDto
import com.example.core.dto.PublishPostDto
import com.example.core.model.SocialMedia
import com.example.core.model.User
import com.example.core.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.function.BiFunction

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
    fun publishPostAllAccounts(userId: Long, postDto: PublishPostDto): List<PostDto> {
        val user = userRepository.findByIdOrElseThrow(userId)

        return publishPostAllAccounts(user.socialMediaSet, postDto)
    }

    @Transactional
    fun publishPost(userId: Long, postDto: PublishPostDto, socialMediaId: Long): PostDto {
        val user = userRepository.findByIdOrElseThrow(userId)

        return user.socialMediaSet.singleOrNull { it.id == socialMediaId }?.let {
            postServiceRegistry.getPostService(it.socialMediaType.getApiService()).publishPost(it, postDto)
        } ?: throw Exception("Could not find a social media account with an id $socialMediaId")
    }

    @Transactional
    fun publishPost(userId: Long, postDto: PublishPostDto, socialMediaIds: Set<Long>): List<PostDto> {
        val user = userRepository.findByIdOrElseThrow(userId)

        return user.socialMediaSet.filter { socialMediaIds.contains(it.id) }
            .map { postServiceRegistry.getPostService(it.socialMediaType.getApiService()).publishPost(it, postDto) }
            .ifEmpty {
                throw Exception("No such social media exist as $socialMediaIds")
            }
    }

    private fun publishPostAllAccounts(tokens: Set<SocialMedia>, post: PublishPostDto): List<PostDto> =
        tokens.map {
            postServiceRegistry.getPostService(it.socialMediaType.getApiService())
                .publishPost(socialMedia = it, postDto = post)
        }

    private fun getPosts(tokens: Set<SocialMedia>): List<PostDto> =
        tokens.flatMap {
            postServiceRegistry.getPostService(it.socialMediaType.getApiService())
                .getPosts(it)
        }

    private fun doPublishPostAction(
        userId: Long,
        postDto: PublishPostDto,
        action: BiFunction<User, PublishPostDto, List<PostDto>>
    ): List<PostDto> {
        val user = userRepository.findByIdOrElseThrow(userId)
        return action.apply(user, postDto)
    }

    private fun doPublishPostActionSingle(
        userId: Long,
        postDto: PublishPostDto,
        action: BiFunction<User, PublishPostDto, PostDto>
    ): PostDto {
        val user = userRepository.findByIdOrElseThrow(userId)
        return action.apply(user, postDto)
    }
}