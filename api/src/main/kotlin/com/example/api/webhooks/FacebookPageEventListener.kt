package com.example.api.webhooks

import com.example.api.service.impl.InstagramApiService
import com.example.core.annotation.Logger
import com.example.core.annotation.Logger.Companion.log
import com.example.core.model.socialmedia.Comment
import com.example.core.model.socialmedia.SocialMediaType
import com.example.core.repository.CommentRepository
import com.example.core.repository.PostRepository
import com.restfb.types.webhook.FeedCommentValue
import com.restfb.types.webhook.FeedLikeValue
import com.restfb.types.webhook.FeedShareValue
import com.restfb.types.webhook.instagram.InstagramCommentsValue
import com.restfb.webhook.AbstractWebhookChangeListener
import org.springframework.context.ApplicationEventPublisher


@Logger
open class FacebookPageEventListener(
    private val postRepository: PostRepository,
    private val commentRepository: CommentRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val instagramApiService: InstagramApiService,
) : AbstractWebhookChangeListener() {

    override fun feedCommentValue(feedCommentValue: FeedCommentValue?) {
        if (feedCommentValue == null || feedCommentValue.isWhatsapp) {
            return
        }

        val postId = feedCommentValue.postId
        val posts = postRepository.selectAllByNativeIdAndSocialMediaType(
            postId,
            setOf(SocialMediaType.FACEBOOK_PAGE, SocialMediaType.INSTAGRAM)
        )
        val post = posts.singleOrNull()
            ?: throw Exception("Found abnormal amount of fb and ig type posts for id $postId")

        commentRepository.save(
            Comment(
                commentId = Comment.CommentKey(feedCommentValue.commentId, SocialMediaType.FACEBOOK_PAGE),
                message = feedCommentValue.message,
                fromId = feedCommentValue.from.id,
                createdTime = feedCommentValue.createdTime.toInstant(),
                post = post,
            )
        )
    }

    override fun feedLikeValue(feedLikeValue: FeedLikeValue?) {
        if (feedLikeValue == null) {
            return
        }

        if (feedLikeValue.isPostLike) {
            feedLikeValue.from
            val posts = postRepository.selectAllByNativeIdAndSocialMediaType(
                feedLikeValue.postId,
                setOf(SocialMediaType.FACEBOOK_PAGE, SocialMediaType.INSTAGRAM)
            )
            val post = posts.singleOrNull()
                ?: throw Exception("Found abnormal amount of fb and ig type posts for id ${feedLikeValue.postId}")

            post.id?.let { postRepository.incrementLikes(it) }

            log.info("Incremented post likes amount for post ${feedLikeValue.postId}")
        } else if (feedLikeValue.isCommentLike) {
            //TODO: retrieve comments
        }
    }

    override fun feedShareValue(feedShareValue: FeedShareValue?) {
        super.feedShareValue(feedShareValue)
    }

    override fun instagramCommentsValue(comment: InstagramCommentsValue?) {
        if (comment == null || comment.isWhatsapp) {
            return
        }

        //TODO: when done with comments for the instagram
        val commentDto = instagramApiService.getPostCommentsFromUnderAppToken(comment.id)
        val post = postRepository.selectAllByNativeIdAndSocialMediaType(
            commentDto.mediaId ?: "",
            socialMediaTypes = setOf(SocialMediaType.INSTAGRAM)
        ).first()
        commentRepository.save(
            Comment(
                commentId = Comment.CommentKey(commentDto.nativeId, SocialMediaType.INSTAGRAM),
                message = comment.text,
                fromId = commentDto.senderDto?.id,
                createdTime = commentDto.createdTime,
                post = post,
            )
        )
    }

}