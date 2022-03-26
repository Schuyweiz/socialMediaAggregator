package com.example.auth.socialmedia.controller

import com.example.auth.socialmedia.service.UserSocialMediaService
import com.example.core.model.User
import com.example.core.dto.SocialMediaDto
import com.example.core.annotation.JwtSecureEndpoint
import com.example.core.annotation.Logger
import com.example.core.annotation.RestControllerJwt
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Logger
@RequestMapping("/user")
@RestControllerJwt
class SocialMediaUserController(
    private val userSocialMediaService: UserSocialMediaService,
) {

    @JwtSecureEndpoint
    @GetMapping("/accounts")
    fun getAuthenticatedAccounts(
        @AuthenticationPrincipal @Parameter(hidden = true) user: User,
    ): List<SocialMediaDto> = userSocialMediaService.getAuthenticatedSocialMedia(user.id)
}
