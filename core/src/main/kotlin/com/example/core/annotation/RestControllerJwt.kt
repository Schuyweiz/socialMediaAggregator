package com.example.core.annotation

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springframework.web.bind.annotation.RestController

@RestController
@SecurityScheme(
    type = SecuritySchemeType.HTTP,
    name = "JWT",
    paramName = org.springframework.http.HttpHeaders.AUTHORIZATION,
    `in` = SecuritySchemeIn.HEADER,
    scheme = "bearer",
)
annotation class RestControllerJwt
