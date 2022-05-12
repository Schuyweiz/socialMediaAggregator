package com.example.tms.service

import com.example.core.model.User
import com.example.core.model.socialmedia.Comment
import com.example.core.repository.CommentRepository
import com.example.tms.model.CommentDto
import com.restfb.types.Comments
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class TmsCommentService(
    private val commentRepository: CommentRepository,
    private val restTemplate: RestTemplate,
) {
//
//    @Transactional
//    fun processExpiredComments(comments: List<Comment>, user: User) {
//        user.webhooks?.commentWebhook?.let {
//            restTemplate.postForLocation(it, comments)
//        }
//        postRepository.deleteAll(expiredPosts)
//    }
//
//    private fun map(comments: List<Comments>): List<CommentDto> =
}