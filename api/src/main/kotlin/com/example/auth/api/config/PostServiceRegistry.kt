package com.example.auth.api.config

import com.example.auth.api.service.SocialMediaPosting

interface PostServiceRegistry {

    fun getPostService(serviceName: String): SocialMediaPosting
}