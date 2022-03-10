package com.example.core.user.repository

import com.example.core.user.model.SocialMediaToken
import org.springframework.data.jpa.repository.JpaRepository

interface SocialMediaTokenRepository : JpaRepository<SocialMediaToken, Long> {

}