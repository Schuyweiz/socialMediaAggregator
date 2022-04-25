package com.example.core.repository

import com.example.core.model.SocialMedia
import com.example.core.model.SocialMediaType
import com.example.core.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface SocialMediaRepository : JpaRepository<SocialMedia, Long>, CrudRepository<SocialMedia, Long> {

    fun findByNativeIdAndSocialMediaType(nativeId: Long, type: SocialMediaType): SocialMedia?

    fun findAllByUserId(userId: Long): Set<SocialMedia>
}