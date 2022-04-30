package com.example.api.webhooks.listeners

import com.example.core.annotation.Logger
import com.example.core.annotation.Logger.Companion.log
import com.example.core.model.SocialMedia
import com.example.core.model.SocialMediaType
import com.example.core.model.socialmedia.Message
import com.example.core.repository.MessageRepository
import com.example.core.repository.SocialMediaRepository
import com.restfb.types.webhook.messaging.MessageItem
import com.restfb.types.webhook.messaging.MessagingParticipant
import com.restfb.webhook.AbstractWebhookMessagingListener
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Logger
open class FacebookMessagingListener(
    private val messageRepository: MessageRepository,
    private val socialMediaRepository: SocialMediaRepository,
) : AbstractWebhookMessagingListener() {

    @Transactional
    override fun message(
        message: MessageItem?,
        recipient: MessagingParticipant?,
        sender: MessagingParticipant?,
        timestamp: Date?
    ) {
        super.message(message, recipient, sender, timestamp)
        if (message?.isDeleted != true && message?.isLike != true) {
            val messageContent = message?.let { it.attachments.firstOrNull()?.url ?: it.text }
            val socialMedia = getMessageSocialMedia(recipient?.id?.toLong() ?: -1L)
            val messageEntity =
                message?.let {
                    return@let Message(
                        content = messageContent.orEmpty(),
                        nativeId = it.mid,
                        socialMedia = socialMedia,
                        createdTime = timestamp?.toInstant() ?: Instant.now()
                    )
                } ?: throw Exception("Something went wrong")

            messageRepository.save(messageEntity)
        } else if (message.isDeleted) {
            val socialMedia = getMessageSocialMedia(recipient?.id?.toLong() ?: -1L)
            val correspondingMessages = messageRepository.findAllByNativeIdAndSocialMedia(message.mid, socialMedia)
            val messageId = correspondingMessages.singleOrNull()?.id
                ?: throw Exception("Amount of messages found is abnormal ${correspondingMessages.size}")
            messageRepository.deleteById(messageId)
        } else {
            log.info("Ignored a message since it is echo")
        }
    }

    private fun getMessageSocialMedia(pageNativeId: Long): SocialMedia {
        val accounts = socialMediaRepository.findAllByNativeIdAndAndSocialMediaTypeIn(
            pageNativeId,
            setOf(SocialMediaType.FACEBOOK_PAGE, SocialMediaType.INSTAGRAM)
        )
        return accounts.firstOrNull()
            ?: throw Exception("Unknown facebook type social media with id $pageNativeId")
    }
}