package com.example.tms.service

import com.example.api.service.PostService
import com.example.core.dto.PublishPostDto
import com.example.core.model.socialmedia.Post
import com.example.core.repository.DelayedPostRepository
import com.example.core.repository.PostRepository
import com.example.tms.model.PostDto
import org.apache.tomcat.util.http.fileupload.FileItem
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItem
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import org.springframework.web.multipart.commons.CommonsMultipartFile
import java.io.File
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class TmsPostService(
    private val postRepository: PostRepository,
    private val restTemplate: RestTemplate,
    private val delayedPostRepository: DelayedPostRepository,
    private val postService: PostService,
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

    @Transactional
    fun publishDelayed() {
        delayedPostRepository.findAllByTimePublishedBefore(Instant.now()).forEach {

            postService.publishPost(
                it.userId!!,
                PublishPostDto(
                    content = it.content,
                    pinTitle = it.pinTitle,
                    pinSectionId = it.pinSectionId,
                    pinBoardId = it.pinBoardId,
                    byteContent = it.attachment,
                    attachment = null,
                ),
                it.socialMediaId!!
            )
        }

    }
}
