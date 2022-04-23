package com.example.api.controller

import com.example.api.dto.PublishCommentDto
import com.example.api.service.CommentingService
import com.example.core.annotation.JwtSecureEndpoint
import com.example.core.annotation.RestControllerJwt
import com.example.core.model.User
import org.springframework.http.MediaType
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@RestControllerJwt
class SocialMediaCommentController(
    private val commentingService: CommentingService
) {

    @JwtSecureEndpoint
    @PostMapping("/api/{socialMediaId}/{postId}/comment/publish", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun publishComment(
        @ModelAttribute commentDto: PublishCommentDto,
        @PathVariable(name = "socialMediaId") socialMediaId: Long,
        @PathVariable(name = "postId") postId: String,
        @AuthenticationPrincipal user: User,
    ) = commentingService.postComment(user.id, socialMediaId, postId, commentDto)

    @JwtSecureEndpoint
    @GetMapping("/api/{socialMediaId}/comment/{postId}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun getComments(
        @PathVariable(name = "postId") postId: String,
        @PathVariable(name = "socialMediaId") socialMediaId: Long,
        @AuthenticationPrincipal user: User,
    ) = commentingService.getAllComments(user.id, postId, socialMediaId)

    @JwtSecureEndpoint
    @PostMapping("/api/comment/{commentId}/respond")
    fun publishComment(
        @PathVariable(name = "commentId") commentId: String,
        @ModelAttribute commentDto: PublishCommentDto,
        @AuthenticationPrincipal user: User,
    ) {

    }
}