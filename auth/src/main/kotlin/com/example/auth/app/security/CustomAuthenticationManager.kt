package com.example.auth.app.security

import com.example.auth.app.jwt.JwtService
import com.example.core.model.User
import com.example.core.annotation.Logger
import com.example.core.annotation.Logger.Companion.log
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Logger
class CustomAuthenticationManager(
    authenticationManager: AuthenticationManager,
    private val jwtService: JwtService,
    private val encoder: PasswordEncoder
) : UsernamePasswordAuthenticationFilter(authenticationManager) {

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        val token = getUsernamePasswordAuthenticationToken(request!!)
        log.info(
            "User in attempt Authentication method in" +
                    " authentication manager with the name ${token.name} and isAuthneticated ${token.isAuthenticated}" +
                    " principal ${token.principal}\n creditentials ${token.credentials}"
        )

        return authenticationManager.authenticate(token)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?,
        authResult: Authentication?,
    ) {
        log.info("Authentication successfully")
        val user = authResult?.principal as User
        val accessToken = jwtService.createAccessToken(user, request!!)
        val refreshToken = jwtService.createRefreshToken(user, request)

        //TODO: rewrite as a map perhaps and rplace with a token service maybe??
        response?.setHeader(ACCESS_TOKEN_NAME, accessToken)
        response?.addCookie(
            bakeRefreshCookie(refreshToken)
        )
        response?.contentType = APPLICATION_JSON_VALUE
    }

    private fun bakeRefreshCookie(refreshToken: String) = Cookie(REFRESH_TOKEN_COOKIE_NAME, refreshToken).apply {
        this.maxAge = jwtService.userRefreshTokenExpiration
        //fixme: figure out the domain thingy
        //this.domain = ".api/auth/"
        this.isHttpOnly = true
        //TODO: set it up for something better wehn done
        this.secure = false
    }

    private fun getUsernamePasswordAuthenticationToken(request: HttpServletRequest) =
        UsernamePasswordAuthenticationToken(
            obtainUsername(request),
            obtainPassword(request)
        )


    companion object {
        private const val REFRESH_TOKEN_COOKIE_NAME = "REFRESH_TOKEN"
        private const val ACCESS_TOKEN_NAME = "access_token"
    }
}