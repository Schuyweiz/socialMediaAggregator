package com.example.api.controller

import com.example.api.service.MediaPostService
import com.example.core.model.User
import com.example.core.model.socialmedia.Post
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping
class SocialMediaPostController(
    private val mediaPostService: MediaPostService,
) {

    @GetMapping("/api/post")
    fun getAllPosts(
       @AuthenticationPrincipal user: User
    ): List<Post> {
        return mediaPostService.getAllUserPosts(user)
    }

}