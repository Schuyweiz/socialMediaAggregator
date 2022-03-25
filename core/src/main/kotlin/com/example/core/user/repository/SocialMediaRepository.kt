package com.example.core.user.repository

import com.example.core.model.SocialMedia
import com.example.core.model.SocialMediaType
import com.example.core.model.socialmedia.SocialMediaDto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SocialMediaRepository : JpaRepository<SocialMedia, Long> {

    fun findByNativeIdAndSocialMediaType(nativeId: Long, type: SocialMediaType): SocialMedia?

    fun findAllByUserId(userId: Long): Set<SocialMedia>
}