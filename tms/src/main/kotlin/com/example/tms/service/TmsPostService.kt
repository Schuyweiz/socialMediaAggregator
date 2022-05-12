package com.example.tms.service

import com.example.core.model.socialmedia.Post
import com.example.core.repository.PostRepository
import com.example.tms.model.PostDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class TmsPostService(
    private val postRepository: PostRepository,
    private val restTemplate: RestTemplate,
) {

    @Transactional
    fun processExpiredPosts() {
        val expiredPosts = postRepository.findAllByCreatedAtBefore(Instant.now().minus(7L, ChronoUnit.DAYS))
        val postsBySocialMedia = expiredPosts.groupBy { it.socialMedia.user }
        postsBySocialMedia.entries.filter { it.key.webhooks?.postWebhook != null }.forEach {
            it.key.webhooks?.postWebhook?.let { it1 -> restTemplate.postForLocation(it1, map(it.value)) }
        }
        postRepository.deleteAll(expiredPosts)
    }

    private fun map(posts: List<Post>): List<PostDto> = posts.map { map(it) }

    private fun map(post: Post): PostDto =
        PostDto(
            (post.nativeId ?: 0) as String,
            post.textContent,
            post.socialMediaType!!,
            post.likes,
            post.comments.size.toLong(),
            post.createdAt ?: Instant.now()
        )
}
