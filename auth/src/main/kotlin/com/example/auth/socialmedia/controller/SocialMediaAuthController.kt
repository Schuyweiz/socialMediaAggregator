package com.example.auth.socialmedia.controller

import com.example.auth.socialmedia.service.FacebookAuthService
import com.example.core.model.User
import com.example.core.dto.PageAuthenticateDto
import com.example.core.dto.SocialMediaDto
import com.example.core.annotation.JwtSecureEndpoint
import com.example.core.annotation.Logger
import com.example.core.annotation.RestControllerJwt
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Logger
@RestControllerJwt
class SocialMediaAuthController(
    private val socialMediaAuthService: FacebookAuthService,
) {


    @GetMapping("/auth/facebook/login")
    fun facebookLoginDialogue(
    ): String = socialMediaAuthService.getLoginDialogueUrl()

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
    ): SocialMediaDto = socialMediaAuthService.authenticateUser(code, user.id)

    @JwtSecureEndpoint
    @GetMapping("/auth/facebook/pages")
    fun getUserPages(
        @AuthenticationPrincipal user: User,
    ): List<PageAuthenticateDto> = socialMediaAuthService.getUserPages(user.id)

    @JwtSecureEndpoint
    @PostMapping("/auth/facebook/pages/{pageId}/authenticate")
    fun authenticateUserPage(
        @AuthenticationPrincipal user: User,
        @PathVariable(name = "pageId") pageId: Long
    ): SocialMediaDto = socialMediaAuthService.authenticateUserPage(user.id, pageId)
}