package com.example.auth.socialmedia.controller

import com.example.auth.socialmedia.service.PinterestAuthService
import com.example.core.annotation.JwtSecureEndpoint
import com.example.core.annotation.RestControllerJwt
import com.example.core.dto.SocialMediaDto
import com.example.core.libs.pinterest.model.PinterestExchangeTokenRequest
import com.example.core.model.User
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import javax.servlet.http.HttpServletRequest

@RestControllerJwt
class PinterestAuthController(
    private val pinterestAuthService: PinterestAuthService,
) {


    @GetMapping("auth/pinterest")
    fun pinterestReceiveCode(
        @RequestParam(name = "code") code: String,
    ): String {
        return code
    }

    @GetMapping("auth/pinterest/token")
    fun pinterestReceiveToken(
        @RequestBody param: String,
    ): String {
        return param
    }

    @GetMapping("auth/pinterest/uri")
    fun getAuthenticationUrl(
    ) = pinterestAuthService.getAuthUri()

    @JwtSecureEndpoint
    @PostMapping("auth/pinterest/token")
    fun exchangeCodeForToken(
        @RequestBody request: PinterestExchangeTokenRequest,
        @AuthenticationPrincipal user: User,
        request2: HttpServletRequest,
    ): SocialMediaDto? {
        return pinterestAuthService.exchangeCodeForToken(request.nativeId, user.id, request.code)
    }
}