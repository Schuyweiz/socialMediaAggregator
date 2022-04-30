package com.example.api.webhooks.listeners

import com.restfb.types.webhook.*
import com.restfb.types.webhook.instagram.InstagramCommentsValue
import com.restfb.webhook.AbstractWebhookChangeListener
import org.springframework.stereotype.Component

@Component
class FacebookPageEventListener(

): AbstractWebhookChangeListener() {

    override fun feedCommentValue(feedCommentValue: FeedCommentValue?) {

    }

    override fun feedLikeValue(feedLikeValue: FeedLikeValue?) {

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