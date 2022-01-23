package com.example.socialmediaaggregator.registration.repository

import com.example.socialmediaaggregator.registration.model.VerificationToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TokenRepository : JpaRepository<VerificationToken, Long> {

    fun getByToken(token: String): VerificationToken?
}