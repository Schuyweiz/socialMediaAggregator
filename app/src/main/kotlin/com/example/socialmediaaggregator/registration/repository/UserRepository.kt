package com.example.socialmediaaggregator.registration.repository

import com.example.socialmediaaggregator.registration.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {

    fun findByMail(mail: String): User?
}