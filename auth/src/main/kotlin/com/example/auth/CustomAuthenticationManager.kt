package com.example.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.core.utils.Logger
import com.example.core.utils.Logger.Companion.log
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Logger
class CustomAuthenticationManager(
    authenticationManager: AuthenticationManager,
    private val algorithm: Algorithm,
) :
    UsernamePasswordAuthenticationFilter(authenticationManager) {

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        val username = request?.getParameter("username")
        val password = request?.getParameter("password")
        log.info("User in attempt Authentication method in authentication manager with the name {}", username)
        val token = UsernamePasswordAuthenticationToken(username, password)
        return authenticationManager.authenticate(token)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        chain: FilterChain?,
        authResult: Authentication?
    ) {
        val user = authResult?.principal as User
        val accessToken = JWT.create()
            .withSubject(user.username)
            .withExpiresAt(Date(System.currentTimeMillis() + 10 * 60 * 1000))
            .withIssuer(request?.requestURL.toString())
            .sign(algorithm)

        val refreshToken = JWT.create()
            .withSubject(user.username)
            .withExpiresAt(Date(System.currentTimeMillis() + 30 * 60 * 1000))
            .withIssuer(request?.requestURL.toString())
            .sign(algorithm)

        //TODO: rewrite as a map perhaps and rplace with a token service maybe??
        response?.setHeader("access_token", accessToken)
        response?.setHeader("refresh_token", refreshToken)
        response?.contentType = APPLICATION_JSON_VALUE
    }
}