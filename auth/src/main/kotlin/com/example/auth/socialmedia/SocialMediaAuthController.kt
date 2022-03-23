package com.example.auth.socialmedia

import com.example.core.model.SocialMedia
import com.example.core.model.User
import com.example.core.model.socialmedia.PageAuthenticateDto
import com.example.core.model.socialmedia.PostDto
import com.example.core.model.socialmedia.SocialMediaDto
import com.example.core.utils.Logger
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@Logger
@RestController
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

    @GetMapping("/auth/facebook/authenticate")
    fun authenticateFacebook(
        @RequestParam(name = "code") code: String,
        @AuthenticationPrincipal user: User
    ): SocialMediaDto = socialMediaAuthService.authenticateUser(code, user.id)

    @GetMapping("/auth/facebook/pages")
    fun getUserPages(
        @AuthenticationPrincipal user: User,
    ): List<PageAuthenticateDto> = socialMediaAuthService.getUserPages(user.id)

    @PostMapping("/auth/facebook/pages/{pageId}/authenticate")
    fun authenticateUserPage(
        @AuthenticationPrincipal user: User,
        @PathVariable(name = "pageId") pageId: Long
    ): SocialMediaDto = socialMediaAuthService.authenticateUserPage(user.id, pageId)
}