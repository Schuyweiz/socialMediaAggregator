package com.example.socialmediaaggregator.auth

import com.auth0.jwt.interfaces.JWTVerifier
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
    private val jwtVerifier: JWTVerifier
) : OncePerRequestFilter() {


    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (request.servletPath.contains("login")) {
            filterChain.doFilter(request, response)
        }

        val authHeader = request.getHeader(AUTHORIZATION) ?: throw JwtAuthenticationException("No jwt token provided.")

        if (!request.servletPath.contains("login") && authHeader.startsWith(BEARER)) {
            if (authHeader.startsWith("Bearer ")) {
                val token = authHeader.substring(BEARER.length)
                val userAuthentication = getUserAuthentication(token)
                log.info("Token verification successful, assigning to the context.")
                SecurityContextHolder.getContext().authentication = userAuthentication
            }
        }
        filterChain.doFilter(request, response)
    }

    private fun getUserAuthentication(token: String): UsernamePasswordAuthenticationToken {
        //fixme: reimplement with private and public key or replace with a properly stored secret
        val decodedToken = jwtVerifier.verify(token)
        val userName = decodedToken.subject
        val user = userService.loadUserByUsername(userName)
        return UsernamePasswordAuthenticationToken(user.username, null, user.authorities)
    }

    companion object {
        private const val BEARER = "Bearer "
    }

}