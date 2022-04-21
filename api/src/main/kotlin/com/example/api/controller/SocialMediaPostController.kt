package com.example.api.controller

import com.example.api.service.PostService
import com.example.core.annotation.JwtSecureEndpoint
import com.example.core.annotation.Logger
import com.example.core.annotation.RestControllerJwt
import com.example.core.dto.PostDto
import com.example.core.dto.PublishPostDto
import com.example.core.dto.PublishPostSelectedAccountsDto
import com.example.core.model.User
import org.springframework.http.MediaType
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*


@RestControllerJwt
@Logger
class SocialMediaPostController(
    private val postService: PostService,
) {

    @JwtSecureEndpoint
    @GetMapping("/api/post")
    fun getAllPosts(
        @AuthenticationPrincipal user: User
    ): List<PostDto> {
        return postService.getAllUserPosts(user)
    }

    @JwtSecureEndpoint
    @PostMapping("/api/post/all", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun publishPost(
        @ModelAttribute postDto: PublishPostDto,
        @AuthenticationPrincipal user: User
    ): List<PostDto> {
        return postService.publishPostAllAccounts(user.id, postDto)
    }

    @JwtSecureEndpoint
    @PostMapping("/api/post/{socialMediaId}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun publishSinglePost(
        @PathVariable(name = "socialMediaId") socialMediaId: Long,
        @ModelAttribute postDto: PublishPostDto,
        @AuthenticationPrincipal user: User,
    ) = postService.publishPost(user.id, postDto, socialMediaId)


    @JwtSecureEndpoint
    @PostMapping("/api/post", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun publishPost(
        @ModelAttribute postDto: PublishPostSelectedAccountsDto,
        @AuthenticationPrincipal user: User,
    ) = postService.publishPost(user.id, postDto.publishPostDto, postDto.socialMediaIds)

}