package com.example.core.user.repository

import com.example.core.model.SocialMedia
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SocialMediaRepository : JpaRepository<SocialMedia, Long> {

}