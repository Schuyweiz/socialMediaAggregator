package com.example.core.user.repository

import com.example.core.user.model.SocialMediaToken
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SocialMediaTokenRepository : JpaRepository<SocialMediaToken, Long> {

}