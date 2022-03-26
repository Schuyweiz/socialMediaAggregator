package com.example.core.annotation

import io.swagger.v3.oas.annotations.security.SecurityRequirement

@SecurityRequirement(name = "JWT")
annotation class JwtSecureEndpoint()
