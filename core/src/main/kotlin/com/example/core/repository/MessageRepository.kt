package com.example.core.repository

import com.example.core.model.SocialMedia
import com.example.core.model.socialmedia.Message
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.Instant

@Repository
interface MessageRepository: JpaRepository<Message, Long> {

    fun findAllByNativeIdAndSocialMedia(nativeId: String, socialMedia: SocialMedia): Set<Message>

    fun findAllByCreatedTimeBefore(createdTime: Instant): Set<Message>
}