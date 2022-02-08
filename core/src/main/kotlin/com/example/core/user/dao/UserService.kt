package com.example.core.user.dao

import com.example.core.user.model.User
import com.example.core.user.model.UserDto
import com.example.core.user.model.VerificationToken


interface UserService {
    fun saveUser(userDto: UserDto): User

    fun createVerificationToken(user: User, token: String): VerificationToken

    fun enableUser(token: String)

    fun getValidToken(token: String): VerificationToken
}