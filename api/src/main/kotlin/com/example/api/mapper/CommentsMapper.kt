package com.example.api.mapper

import com.example.api.dto.CommentDto
import com.example.api.dto.SenderDto
import com.restfb.types.User
import com.restfb.types.Comment as FbComment
import org.springframework.stereotype.Component

@Component
class CommentsMapper {

    fun mapToCommentDto(comment: FbComment) = CommentDto(
        id = null,
        nativeId = comment.id,
        content = comment.message,
        senderDto = (comment.from.asUser).let {
            return@let SenderDto(it.id, it.name, it.picture?.url.orEmpty())
        }
    )
}