package com.example.api.webhooks.listeners

import com.example.api.webhooks.IncrementPostLikesEvent
import com.example.core.annotation.Logger
import com.example.core.annotation.Logger.Companion.log
import com.example.core.model.SocialMediaType
import com.example.core.repository.PostRepository
import com.restfb.types.webhook.*
import com.restfb.types.webhook.instagram.InstagramCommentsValue
import com.restfb.webhook.AbstractWebhookChangeListener
import org.springframework.context.ApplicationEventPublisher
import org.springframework.transaction.annotation.Transactional


@Logger
open class FacebookPageEventListener(
    private val postRepository: PostRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
) : AbstractWebhookChangeListener() {

    override fun feedCommentValue(feedCommentValue: FeedCommentValue?) {

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

    override fun feedStatusValue(feedStatusValue: FeedStatusValue?) {
        super.feedStatusValue(feedStatusValue)
    }

    override fun feedEventValue(feedEventValue: FeedEventValue?) {
        super.feedEventValue(feedEventValue)
    }

    override fun feedPostValue(feedPostValue: FeedPostValue?) {
        super.feedPostValue(feedPostValue)
    }

    override fun feedReactionValue(feedReactionValue: FeedReactionValue?) {
        super.feedReactionValue(feedReactionValue)
    }

    override fun feedShareValue(feedShareValue: FeedShareValue?) {
        super.feedShareValue(feedShareValue)
    }

    override fun instagramCommentsValue(instagramCommentsValue: InstagramCommentsValue?) {
        super.instagramCommentsValue(instagramCommentsValue)
    }

    override fun ratingsReactionValue(ratingsReactionValue: RatingsReactionValue?) {
        super.ratingsReactionValue(ratingsReactionValue)
    }

    override fun ratingsLikeValue(ratingsLikeValue: RatingsLikeValue?) {
        super.ratingsLikeValue(ratingsLikeValue)
    }

    override fun ratingsCommentValue(ratingsCommentValue: RatingsCommentValue?) {
        super.ratingsCommentValue(ratingsCommentValue)
    }

}