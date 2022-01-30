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
    //TODO: replace second with last name
    val secondName: String,
    @ValidEmail
    @NotNull
    @NotEmpty
    //TODO: replace with email
    val mail: String,
    @NotNull
    @NotEmpty
    //TODO: remove, unnecessary
    val username: String,
    @NotNull
    @NotEmpty
    val password: String,
)