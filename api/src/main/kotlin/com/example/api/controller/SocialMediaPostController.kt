package com.example.api.controller

import com.example.api.service.MediaPostService
import com.example.core.model.User
import com.example.core.model.socialmedia.PostDto
import com.example.core.model.socialmedia.PublishPostDto
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
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
    ): List<PostDto> {
        return mediaPostService.getAllUserPosts(user)
    }

    @PostMapping("/api/post")
    fun publishPost(
        @RequestBody postDto: PublishPostDto,
        @AuthenticationPrincipal user: User
    ): List<PostDto> {
        return mediaPostService.publishPost(user.id, postDto)
    }

}