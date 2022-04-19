package com.example.auth.socialmedia.controller

import com.example.auth.socialmedia.service.FacebookBasicAuthService
import com.example.core.annotation.JwtSecureEndpoint
import com.example.core.annotation.Logger
import com.example.core.annotation.RestControllerJwt
import com.example.core.dto.SocialMediaDto
import com.example.core.model.User
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Logger
@RestControllerJwt
class FacebookBasicAuthController(
    private val facebookBasicAuthService: FacebookBasicAuthService,
) {
    @GetMapping("/auth/facebook/login")
    fun facebookLoginDialogue(
    ): String = facebookBasicAuthService.getLoginDialogueUrl()

    @GetMapping("auth/facebook")
    fun getFacebookCode(
        @RequestParam(name = "code") code: String,
    ): String {
        //socialMediaAuthService.authenticateUser(code)
        return code;
    }

    @JwtSecureEndpoint
    @GetMapping("/auth/facebook/authenticate")
    fun authenticateFacebook(
        @RequestParam(name = "code") code: String,
        @AuthenticationPrincipal user: User
    ): SocialMediaDto = facebookBasicAuthService.authenticateUser(code, user.id)
}