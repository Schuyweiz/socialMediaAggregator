package com.example.core.repository

import com.example.core.model.SocialMedia
import com.example.core.model.socialmedia.SocialMediaType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SocialMediaRepository : JpaRepository<SocialMedia, Long> {

    fun findByNativeIdAndSocialMediaType(nativeId: Long, type: SocialMediaType): SocialMedia?

    fun findAllByUserId(userId: Long): Set<SocialMedia>

    fun findAllByNativeIdAndAndSocialMediaTypeIn(
        nativeId: Long,
        socialMediaTypes: Set<SocialMediaType>
    ): Set<SocialMedia>
}