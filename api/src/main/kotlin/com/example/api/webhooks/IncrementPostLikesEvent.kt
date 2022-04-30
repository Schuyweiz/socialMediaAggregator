package com.example.api.webhooks

import com.example.core.model.socialmedia.Post
import org.springframework.context.ApplicationEvent

class IncrementPostLikesEvent(post: Post): ApplicationEvent(post) {
}