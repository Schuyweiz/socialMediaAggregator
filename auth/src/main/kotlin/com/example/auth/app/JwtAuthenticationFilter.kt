package com.example.auth.app

import com.example.auth.app.jwt.JwtAuthenticationException
import com.example.auth.app.jwt.JwtService
import com.example.core.utils.Logger
import com.example.core.utils.Logger.Companion.log
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Logger
class JwtAuthenticationFilter(
    private val userService: UserDetailsService,
    private val jwtService: JwtService,
    private val jwtAuthWhitelist: Set<String>,
) : OncePerRequestFilter() {

    override fun shouldNotFilter(request: HttpServletRequest): Boolean
    = jwtAuthWhitelist.contains(request.servletPath)


    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        if (request.servletPath.contains("login")) {
            filterChain.doFilter(request, response)
            return
        }

        val authHeader =
            request.getHeader(AUTHORIZATION) ?: throw JwtAuthenticationException("Authorization header is empty")

        if (!request.servletPath.contains("login") && authHeader.startsWith(BEARER)) {
            val token = authHeader.substring(BEARER.length)
            val userAuthentication = getUserAuthentication(token)
            log.info("Token verification successful, assigning to the context.")
            SecurityContextHolder.getContext().authentication = userAuthentication
        }
        filterChain.doFilter(request, response)
    }

    private fun getUserAuthentication(token: String): UsernamePasswordAuthenticationToken {
        //fixme: reimplement with private and public key or replace with a properly stored secret
        val decodedToken = jwtService.verifyToken(token)
        val userName = decodedToken.subject
        val user = userService.loadUserByUsername(userName)
        return UsernamePasswordAuthenticationToken(user, null, user.authorities)
    }

    companion object {
        private const val BEARER = "Bearer "
    }

}