package com.example.core.user.repository

import com.example.core.model.User
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import javax.persistence.EntityNotFoundException

@Repository
interface UserRepository : JpaRepository<User, Long> {

    @EntityGraph(type=EntityGraph.EntityGraphType.LOAD, attributePaths = ["socialMediaSet"])
    fun findByEmail(mail: String): User?

    //todo: maybe a custom exception
    @JvmDefault
    fun findByIdOrElseThrow(id: Long): User = findById(id).orElseThrow()


    @EntityGraph(type=EntityGraph.EntityGraphType.LOAD, attributePaths = ["socialMediaSet"])
    @JvmDefault
    @Query("select u from app_user u where u.id = :id")
    fun findByIdWithTokens(id: Long): User?

    @JvmDefault
    fun findByIdWithSocialMediaOrThrow(id: Long): User{
        return findByIdWithTokens(id) ?: throw EntityNotFoundException()
    }
}