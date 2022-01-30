package com.example.core.model

import com.example.core.utils.validation.ValidEmail
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