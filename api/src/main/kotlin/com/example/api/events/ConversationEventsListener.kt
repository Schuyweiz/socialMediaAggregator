package com.example.api.events

import com.example.core.repository.MessageRepository
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class ConversationEventsListener(
    private val messagesRepository: MessageRepository
) {

    @EventListener
    fun onMessagesRequestedEvent(event: ConversationGetMessagesEvent) {
        messagesRepository.saveAll(event.messages)
    }
}