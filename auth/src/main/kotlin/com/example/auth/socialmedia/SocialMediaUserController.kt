package com.example.auth.socialmedia

import com.example.core.model.User
import com.example.core.model.socialmedia.SocialMediaDto
import com.example.core.utils.Logger
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Logger
@RestController
@RequestMapping("/user")
class SocialMediaUserController(
    private val userSocialMediaService: UserSocialMediaService,
) {

    @GetMapping("/accounts")
    fun getAuthenticatedAccounts(
        @AuthenticationPrincipal user: User,
    ): List<SocialMediaDto> = userSocialMediaService.getAuthenticatedSocialMedia(user.id)

}