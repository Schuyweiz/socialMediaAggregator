package com.example.socialmediaaggregator.utils

import com.example.socialmediaaggregator.registration.model.User
import com.example.socialmediaaggregator.registration.model.UserDto
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class UserMapper(
    val passwordEncoder: PasswordEncoder,
) {

    fun userDtoToUser(userDto: UserDto) = User(
        firstName = userDto.firstName,
        secondName = userDto.secondName,
        username = userDto.username,
        mail = userDto.mail,
        passwordHash = passwordEncoder.encode(userDto.password),
    )
}