package com.example.core.repository

import com.example.core.model.DelayedPost
import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant

interface DelayedPostRepository: JpaRepository<DelayedPost, Long> {

    fun findAllByTimePublishedBefore(timePublished: Instant): List<DelayedPost>
}