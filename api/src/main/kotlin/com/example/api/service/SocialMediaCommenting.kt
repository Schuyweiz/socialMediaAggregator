package com.example.api.service

import com.example.api.dto.comment.CommentDto
import com.example.api.dto.PublishCommentDto
import com.example.core.model.SocialMedia

interface SocialMediaCommenting {

    fun getPostComments(socialMedia: SocialMedia, postId: String): List<CommentDto>

    fun publishComment(socialMedia: SocialMedia, postId: String, commentDto: PublishCommentDto): CommentDto

    fun respondToComment(socialMedia: SocialMedia, commentId: String, commentDto: PublishCommentDto): CommentDto
}