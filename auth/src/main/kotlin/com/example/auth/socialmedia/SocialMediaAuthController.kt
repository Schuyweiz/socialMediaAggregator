package com.example.auth.socialmedia

import com.example.core.model.User
import com.example.core.model.socialmedia.FacebookPage
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
        @AuthenticationPrincipal user: User?
    ) {
        socialMediaAuthService.authenticateUser(code,user!!)
    }

    @GetMapping("/auth/facebook/pages")
    fun getUserPages(
        @AuthenticationPrincipal user: User,
    ): List<FacebookPage> = socialMediaAuthService.getUserPages(user.id)

    @PostMapping("/auth/facebook/pages/{pageId}/authenticate")
    fun authneticateUserPage(
        @AuthenticationPrincipal user: User,
        @PathVariable(name = "pageId") pageId: Long
    ){
        socialMediaAuthService.authenticateUserPage(user.id, pageId)
    }
}