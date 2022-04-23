package com.example.api.config

import com.example.api.service.SocialMediaCommenting

interface CommentServiceRegistry {

    fun getCommentingService(serviceName: String): SocialMediaCommenting

}