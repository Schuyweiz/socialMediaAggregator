package com.example.core.repository

import com.example.core.model.User
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = ["socialMediaSet"])
    fun findByEmail(mail: String): User?
}