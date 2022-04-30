package com.example.api.webhooks

import com.example.core.model.socialmedia.Post
import com.example.core.repository.PostRepository
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class FacebookApplicationEventListener(
    private val postRepository: PostRepository,
) {

    @EventListener
    fun incrementpostlikes(event: IncrementPostLikesEvent) {
        val post = event.source as Post
        post.incrementLikes()
        postRepository.save(post)
    }
}