package com.example.api.events

import com.example.core.model.SocialMedia
import com.example.core.model.socialmedia.Message
import org.springframework.context.ApplicationEvent

class ConversationGetMessagesEvent(val messages: List<Message>): ApplicationEvent(messages) {
}