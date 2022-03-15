package com.example.core.user.model

import com.example.core.validation.ValidEmail
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

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