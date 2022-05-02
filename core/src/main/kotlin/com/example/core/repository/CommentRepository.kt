package com.example.core.repository

import com.example.core.model.socialmedia.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository: JpaRepository<Comment, Comment.CommentKey> {
}