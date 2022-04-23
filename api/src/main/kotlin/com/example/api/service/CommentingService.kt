package com.example.api.service

import com.example.api.config.CommentServiceRegistry
import com.example.api.dto.PublishCommentDto
import com.example.core.model.User
import com.example.core.repository.SocialMediaRepository
import com.example.core.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CommentingService(
    private val userRepository: UserRepository,
    private val registry: CommentServiceRegistry,
    private val socialMediaRepository: SocialMediaRepository,
) {

    fun getAllComments(userId: Long, postId: String, socialMediaId: Long) = doCommentingAction(userId) { user: User ->
        user.socialMediaSet.singleOrNull { it.id == socialMediaId }?.let {
            registry.getCommentingService(it.socialMediaType.getApiService()).getPostComments(it, postId)
        }
    }

    private fun <T> doCommentingAction(userId: Long, action: java.util.function.Function<User, T>): T =
        with(userRepository.findByIdOrElseThrow(userId)) {
            return action.apply(this)
        }


    @Transactional
    fun postComment(userId: Long, socialMediaId: Long, postId: String, commentDto: PublishCommentDto) =
        doCommentingAction(userId) { user ->
            user.socialMediaSet.singleOrNull { it.id == socialMediaId }?.let {
                registry.getCommentingService(it.socialMediaType.getApiService())
                    .publishComment(it, postId, commentDto)
            }
        }
}