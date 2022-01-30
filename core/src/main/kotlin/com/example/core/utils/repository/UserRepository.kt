package com.example.core.utils.repository

import com.example.core.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {

    fun findByMail(mail: String): User?
}