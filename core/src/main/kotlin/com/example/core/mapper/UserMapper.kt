package com.example.core.mapper

import com.example.core.model.User
import com.example.core.dto.UserDto
import org.springframework.stereotype.Component

@Component
class UserMapper(
) {

    fun userDtoToUser(userDto: UserDto, password: String) = User(
        firstName = userDto.firstName,
        lastName = userDto.lastName,
        email = userDto.email,
        socialMediaSet = mutableSetOf(),
        userPassword = password
    )
}