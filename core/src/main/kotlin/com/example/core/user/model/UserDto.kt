package com.example.core.user.model

import com.example.core.utils.DefaultCtor
import com.example.core.validation.ValidEmail
import net.bytebuddy.implementation.bind.annotation.AllArguments
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