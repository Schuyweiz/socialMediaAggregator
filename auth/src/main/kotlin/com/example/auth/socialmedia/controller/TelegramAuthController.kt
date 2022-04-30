package com.example.auth.socialmedia.controller

import com.example.auth.socialmedia.service.TgAuthService
import com.example.core.annotation.JwtSecureEndpoint
import com.example.core.annotation.RestControllerJwt
import com.example.core.model.User
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@RestControllerJwt
class TelegramAuthController(
    private val tgAuthService: TgAuthService,
) {


    @JwtSecureEndpoint
    @PostMapping("auth/tg/phone/{number}")
    fun authByPhoneNumber(
        @PathVariable(name = "number") number: String,
        @AuthenticationPrincipal user: User,
    ) = tgAuthService.authPhoneNumber(number, user.id)


    @JwtSecureEndpoint
    @PostMapping("auth/{accountId}/tg/code/{code}")
    fun authByCode(
        @PathVariable(name = "code") code: String,
        @PathVariable(name = "accountId") accountId: Long,
        @AuthenticationPrincipal user: User,
    ) = tgAuthService.authCode(code, user.id, accountId)
}