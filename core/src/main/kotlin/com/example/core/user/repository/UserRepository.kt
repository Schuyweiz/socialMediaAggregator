package com.example.core.user.repository

import com.example.core.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {


    fun findByEmail(mail: String): User?

    //todo: maybe a custom exception
    @JvmDefault
    fun findByIdOrElseThrow(user: User): User = findById(user.id).orElseThrow()
}