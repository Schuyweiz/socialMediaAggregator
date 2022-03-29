package com.example.core.repository

import com.example.core.model.socialmedia.Message
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository: JpaRepository<Message, Long> {
}