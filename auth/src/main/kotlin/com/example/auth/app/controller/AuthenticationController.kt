package com.example.auth.app.controller

import com.example.auth.app.jwt.JwtService
import com.example.auth.util.CookieUtil
import com.example.core.annotation.RestControllerJwt
import com.example.core.dto.LoginRequestDto
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import io.swagger.v3.oas.annotations.parameters.RequestBody as SwaggerBody

@RestControllerJwt
class AuthenticationController(
    private val jwtService: JwtService,
) {

    @GetMapping("/auth/refresh")
    fun refreshAccessToken(
        response: HttpServletResponse,
        request: HttpServletRequest,
    ): ResponseEntity<HttpStatus> {
        val decodedJWT = jwtService.verifyRefreshToken(request.getHeader("REFRESH_TOKEN"))
        val refreshToken = jwtService.updateRefreshToken(decodedJWT)
        val accessToken = jwtService.updateAccessToken(decodedJWT)

        response.addCookie(CookieUtil.makeCookie(refreshToken, "REFRESH_TOKEN", jwtService.userRefreshTokenExpiration))
        response.addHeader("ACCESS_TOKEN", accessToken)

        return ResponseEntity.ok().body(HttpStatus.OK);
    }

    @PostMapping("/login")
    fun login(
        @RequestBody @SwaggerBody(
            content = [Content(
                mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                schema = Schema(implementation = LoginRequestDto::class, name = "login dto")
            )]
        )
        loginRequestDto: LoginRequestDto,
        request: HttpServletRequest, response: HttpServletResponse
    ): String {
        return response.getHeader(HttpHeaders.AUTHORIZATION);
    }
}