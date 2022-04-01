package com.example.api.controller

import com.example.api.dto.SendMessageDto
import com.example.api.service.ConversationsService
import com.example.core.annotation.JwtSecureEndpoint
import com.example.core.annotation.RestControllerJwt
import com.example.core.dto.LoginRequestDto
import com.example.core.model.User
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import org.springdoc.api.annotations.ParameterObject
import org.springframework.http.MediaType
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.multipart.MultipartFile

@RestControllerJwt
class SocialMediaConversationController(
    private val conversationsService: ConversationsService,
) {

    @JwtSecureEndpoint
    @GetMapping("/api/conversations")
    fun getAllUserConversations(
        @AuthenticationPrincipal user: User
    ) = conversationsService.getAllUserConversations(user.id)

    @JwtSecureEndpoint
    @GetMapping("/api/conversations/{socialMediaId}")
    fun getAllConversationsBySocialMediaAccount(
        @PathVariable(name = "socialMediaId", required = true) socialMediaId: Long
    ) = conversationsService.getAllConversationsBySocialMediaId(socialMediaId)

    @JwtSecureEndpoint
    @GetMapping("api/{socialMediaId}/conversations/{conversationId}/messages")
    fun getConversationWithMessages(
        @PathVariable(name = "socialMediaId", required = true) socialMediaId: Long,
        @PathVariable(name = "conversationId", required = true) conversationId: String,
    ) = conversationsService.getConversationWithMessages(socialMediaId, conversationId)

    @JwtSecureEndpoint
    @PostMapping(
        "api/{socialMediaId}/conversations/{conversationId}/send",
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    fun sendMessageToConversation(
        @PathVariable(name = "socialMediaId", required = true) socialMediaId: Long,
        @ModelAttribute request: SendMessageDto,
    ) = conversationsService.sendMessage(socialMediaId, request)
}
