package com.example.api.mapper

import com.example.api.dto.comment.CommentDto
import com.example.api.dto.SenderDto
import com.restfb.json.JsonObject
import org.springframework.stereotype.Component
import com.restfb.types.Comment as FbComment

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

    fun mapToCommentDto(comment: JsonObject) = CommentDto(
        id = null,
        nativeId = comment.getString("id", ""),
        content = comment.getString("message", ""),
        senderDto = mapSenderDto(comment.get("from").asObject())
    )

    private fun mapSenderDto(sender: JsonObject) =
        SenderDto(sender.getString("id", ""), sender.getString("username", ""), null)
}