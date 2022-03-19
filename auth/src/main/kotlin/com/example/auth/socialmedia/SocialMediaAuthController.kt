package com.example.auth.socialmedia

import com.example.core.user.model.User
import com.example.core.utils.Logger
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

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

    @GetMapping("auth/facebook/authenticate")
    fun authenticateFacebook(
        @RequestParam(name = "code") code: String,
        @AuthenticationPrincipal user: User?
    ) {
        socialMediaAuthService.authenticateUser(code,user!!)
    }
}