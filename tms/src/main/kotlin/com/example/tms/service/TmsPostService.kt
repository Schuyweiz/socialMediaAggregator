package com.example.tms.service

import com.example.core.repository.PostRepository
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class TmsPostService(
    private val postRepository: PostRepository,
    private val restTemplate: RestTemplate,
) {

    fun processExpiredPosts() {
        val expiredPosts = postRepository.findAllByCreatedAtBefore(Instant.now().minus(7L, ChronoUnit.DAYS))
        val postsBySocialMedia = expiredPosts.groupBy { it.socialMedia.user }
        postsBySocialMedia.entries.filter { it.key.webhooks?.postWebhook != null }.forEach {
            it.key.webhooks?.postWebhook?.let { it1 -> restTemplate.postForLocation(it1, it.value) }
        }
    }
}
