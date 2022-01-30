package com.example.core.utils.repository

import com.example.core.model.VerificationToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TokenRepository : JpaRepository<VerificationToken, Long> {

    fun getByToken(token: String): VerificationToken?
}