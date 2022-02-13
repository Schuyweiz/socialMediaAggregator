package com.example.auth.app

import com.example.auth.app.jwt.JwtService
import com.example.auth.util.CookieUtil
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
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
}