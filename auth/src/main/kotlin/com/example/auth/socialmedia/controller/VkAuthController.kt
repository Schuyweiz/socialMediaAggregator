package com.example.auth.socialmedia.controller

import com.example.auth.socialmedia.service.VkAuthService
import com.example.core.annotation.JwtSecureEndpoint
import com.example.core.annotation.RestControllerJwt
import com.example.core.model.User
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@RestControllerJwt
class VkAuthController(
    private val vkAuthService: VkAuthService,
) {

    @GetMapping("auth/vk/url")
    fun getVkAuthUrl(
    ): String? = vkAuthService.getAuthCodeLink()

    @GetMapping("auth/vk")
    fun getVkAuthCode(
        @RequestParam(name = "code") code: String,
    ) = code

    @JwtSecureEndpoint
    @PostMapping("auth/vk/authenticate")
    fun vkAuthenticate(
        @AuthenticationPrincipal user: User,
        @RequestParam(name = "code") code: String,
    ) = vkAuthService.authUser(code, user.id)

    @JwtSecureEndpoint
    @GetMapping("auth/vk/{accountId}/groups")
    fun getVkGroups(
        @AuthenticationPrincipal user: User,
        @PathVariable(name = "accountId", required = true) accountId: Long
    ) = vkAuthService.getUserGroups(user.id, accountId)

    @GetMapping("auth/vk/groups")
    fun getGroupAuthUrl(
        @RequestParam(name = "groupIds") groupIds: List<Int>,
    ) = vkAuthService.getGroupAuthLink(groupIds)

    @JwtSecureEndpoint
    @PostMapping("auth/vk/groups")
    fun authenticateGroups(
        @AuthenticationPrincipal user: User,
        @RequestParam(name = "code") code: String,
    ) = vkAuthService.authGroups(code, user.id)
}