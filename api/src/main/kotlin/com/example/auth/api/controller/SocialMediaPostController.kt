package com.example.auth.api.controller

import com.example.auth.api.service.MediaPostService
import com.example.core.user.model.User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/post")
class SocialMediaPostController(
    private val mediaPostService: MediaPostService,
) {

    @GetMapping
    fun getAllPosts(
        user: User
    ) = mediaPostService.getAllUserPosts(user)

}