package com.example.core.dto

import com.example.core.annotation.DefaultCtor
import com.example.core.annotation.ValidEmail
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@DefaultCtor
data class UserDto(
    @NotNull
    @NotEmpty
    val firstName: String,

    @NotNull
    @NotEmpty
    val lastName: String,

    @ValidEmail
    @NotNull
    @NotEmpty
    val email: String,

    @NotNull
    @NotEmpty
    val password: String,
)