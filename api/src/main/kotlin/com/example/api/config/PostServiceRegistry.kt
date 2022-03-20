package com.example.api.config

import com.example.api.service.SocialMediaPosting

interface PostServiceRegistry {

    fun getPostService(serviceName: String): SocialMediaPosting
}