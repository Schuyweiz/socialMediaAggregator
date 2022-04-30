package com.example.api.webhooks.controller

import com.example.api.webhooks.service.FacebookwebhookService
import com.example.core.annotation.Logger
import com.example.core.annotation.Logger.Companion.log
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Logger
class FacebookWebhooksController(
    private val facebookwebhookService: FacebookwebhookService,
) {


    @PostMapping("facebook/webhook")
    fun receivePageWebhook(
        @RequestBody eventContent: String
    ) {
        log.info(eventContent)
        facebookwebhookService.processMessageWebhook(eventContent)
    }

    @GetMapping("facebook/webhook")
    fun verifyPageWebHook(
        @RequestParam(name = "hub.challenge") verification: String
    ): String {
        log.info(verification)
        return verification
    }
}