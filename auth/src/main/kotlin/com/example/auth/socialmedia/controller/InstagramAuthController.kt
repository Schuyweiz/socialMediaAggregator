package com.example.auth.socialmedia.controller

import com.example.auth.socialmedia.service.InstagramAuthService
import com.example.core.annotation.JwtSecureEndpoint
import com.example.core.annotation.Logger
import com.example.core.annotation.RestControllerJwt
import com.example.core.model.User
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@Logger
@RestControllerJwt
class InstagramAuthController(
    private val instagramAuthService: InstagramAuthService,
) {
    @JwtSecureEndpoint
    @GetMapping("/auth/instagram/accounts")
    fun getInstagramAccounts(
        @AuthenticationPrincipal user: User,
    ) = instagramAuthService.getInstagramPages(user.id)

    @JwtSecureEndpoint
    @PostMapping("/auth/instagram/accounts/{accountId}/authenticate")
    fun authenticateInstagramAccount(
        @AuthenticationPrincipal user: User,
        @PathVariable(name = "accountId") accountId: Long
    ) = instagramAuthService.authenticateInstagramPage(user.id, accountId)
}