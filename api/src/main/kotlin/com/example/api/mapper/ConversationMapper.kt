package com.example.api.mapper

import com.example.api.dto.*
import com.example.api.dto.message.MessageDto
import com.example.core.model.SocialMedia
import com.example.core.model.socialmedia.Message
import org.springframework.stereotype.Component
import com.restfb.types.Conversation as RestFbConversation
import com.restfb.types.Message as RestFbMessage

@Component
class ConversationMapper {

    fun convertRestFbConversationToDto(restFbConversation: RestFbConversation): ConversationDto = ConversationDto(
        conversationId = restFbConversation.id,
        updatedTime = restFbConversation.updatedTime,
        messageCount = restFbConversation.messageCount,
        unreadCount = restFbConversation.unreadCount,
        participants = restFbConversation.participants,
        canReply = restFbConversation.canReply,
        createdTime = null
    )

    fun convertRestFbConversationToConversationWithMessagesDto(restFbConversation: RestFbConversation): ConversationWithMessagesDto =
        ConversationWithMessagesDto(
            conversationDto = convertRestFbConversationToDto(restFbConversation),
            messagesDto = convertRestFbMessagesToMessagesDto(restFbConversation.messages),
        )


    fun convertRestFbMessagesToMessagesDto(restFbMessageList: List<RestFbMessage>): List<MessageDto> =
        restFbMessageList.map {
            convertRestFbMessageToMessageDto(it)
        }

    fun convertRestFbMessageToMessageDto(restFbMessage: RestFbMessage): MessageDto = MessageDto(
        nativeId = restFbMessage.id,
        message = restFbMessage.message,
        participantDto = ParticipantDto(
            nativeId = restFbMessage.from.id ?: restFbMessage.from.userId,
            name = restFbMessage.from.name ?: restFbMessage.from.username
        ),
        createdTime = restFbMessage.createdTime,
        attachment = restFbMessage.attachments.firstOrNull().let {
            return@let RecieveAttachmentDto(it?.id, it?.mimeType, it?.size, it?.imageData?.url)
        }
    )


    fun convertRestFbConversationToMessages(
        conversationWithMessagesDto: ConversationWithMessagesDto,
        socialMedia: SocialMedia
    ): List<Message> = conversationWithMessagesDto.messagesDto.map {
        convertRestFbMessageToMessage(it, socialMedia)
    }

    fun convertRestFbMessageToMessage(messagesDto: MessageDto, socialMedia: SocialMedia): Message = Message(
        content = messagesDto.message.plus(messagesDto.attachment?.url),
        nativeId = messagesDto.nativeId,
        createdTime = messagesDto.createdTime.toInstant(),
        socialMedia = socialMedia,
    )

    fun convertMessageDtoToReceiveAttachment(dto: MessageDto?) = RecieveAttachmentDto(
        nativeId = dto?.nativeId,
        mimeType = dto?.attachment?.mimeType,
        size = dto?.attachment?.size,
        url = dto?.attachment?.url,
        attachmentMessageId = dto?.nativeId
    )
}