package com.example.api.events

import com.example.core.model.SocialMedia
import com.example.core.dto.PostDto
import org.springframework.context.ApplicationEvent

class PostRequestedEvent(val socialMedia: SocialMedia, val posts: List<PostDto>) :
    ApplicationEvent(socialMedia) {
}