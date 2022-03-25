package com.example.api.events

import com.example.core.user.repository.PostRepository
import com.example.core.utils.PostDtoMapper
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class PostEventListener(
    private val mapper: PostDtoMapper,
    private val postRepository: PostRepository,
) {

    @EventListener
    fun persistPostsUponDemand(event: PostRequestedEvent) {
        event.posts.map { mapper.mapToPost(it, event.socialMedia) }.let {
            postRepository.saveAll(it)
        }
    }
}