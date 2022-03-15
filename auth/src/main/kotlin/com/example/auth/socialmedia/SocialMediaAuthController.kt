package com.example.auth.socialmedia

import com.example.core.config.FacebookConfiguration
import com.example.core.utils.Logger
import com.restfb.DefaultFacebookClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient

@Logger
@RestController
class SocialMediaAuthController(
    private var webClient: WebClient,
    private var facebookClient: DefaultFacebookClient,
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
    ) {
        socialMediaAuthService.authenticateUser(code)
    }
}