package com.example.core.user.dao

import com.example.core.model.User
import com.example.core.model.UserDto
import com.example.core.model.VerificationToken


interface UserService {
    fun saveUser(userDto: UserDto): User

    fun createVerificationToken(user: User, token: String): VerificationToken

    fun enableUser(token: String)

    fun getValidToken(token: String): VerificationToken
}