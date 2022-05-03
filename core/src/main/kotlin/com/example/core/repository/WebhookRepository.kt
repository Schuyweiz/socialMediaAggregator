package com.example.core.repository

import com.example.core.model.Webhooks
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WebhookRepository : JpaRepository<Webhooks, Long> {
}