package com.example.core.utils

import com.example.core.model.User
import com.example.core.model.UserDto
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class UserMapper(
) {

    fun userDtoToUser(userDto: UserDto, password: String) = User(
        firstName = userDto.firstName,
        lastName = userDto.lastName,
        email = userDto.email,
        socialMediaTokens = mutableSetOf(),
        userPassword = password
    )
}