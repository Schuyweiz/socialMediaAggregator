package com.example.auth.socialmedia.controller

import com.example.auth.socialmedia.service.FacebookAuthService
import com.example.core.annotation.JwtSecureEndpoint
import com.example.core.annotation.Logger
import com.example.core.annotation.RestControllerJwt
import com.example.core.dto.PageAuthenticateDto
import com.example.core.dto.SocialMediaDto
import com.example.core.model.User
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@Logger
@RestControllerJwt
class FacebookAuthController(
    private val facebookAuthService: FacebookAuthService,
) {

    @JwtSecureEndpoint
    @GetMapping("/auth/facebook/pages")
    fun getUserPages(
        @AuthenticationPrincipal user: User,
    ): List<PageAuthenticateDto> = facebookAuthService.getUserPages(user.id)

    @JwtSecureEndpoint
    @PostMapping("/auth/facebook/pages/{pageId}/authenticate")
    fun authenticateUserPage(
        @AuthenticationPrincipal user: User,
        @PathVariable(name = "pageId") pageId: Long
    ): SocialMediaDto = facebookAuthService.authenticateUserPage(user.id, pageId)
}