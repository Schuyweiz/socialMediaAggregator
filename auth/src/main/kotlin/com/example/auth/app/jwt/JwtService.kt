package com.example.auth.app.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.JWTVerifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Service
import java.util.*
import javax.servlet.http.HttpServletRequest

@Service
class JwtService(
    val algorithm: Algorithm,
    val jwtVerifier: JWTVerifier,
    @Value("3000")
    val userRefreshTokenExpiration: Int,
) {


    fun createToken(user: User, request: HttpServletRequest): String = JWT.create()
        .withSubject(user.username)
        .withExpiresAt(Date(System.currentTimeMillis() + 10 * 60 * 1000))
        .withIssuer(request.requestURL.toString())
        .sign(algorithm)

    fun createRefreshToken(user: User, request: HttpServletRequest): String = JWT.create()
        .withSubject(user.username)
        .withExpiresAt(Date(System.currentTimeMillis() + 30 * 60 * 1000))
        .withIssuer(request.requestURL.toString())
        .sign(algorithm)

    fun verifyToken(token: String): DecodedJWT = jwtVerifier.verify(token)
}