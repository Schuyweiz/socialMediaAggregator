package com.example.api.controller

import com.example.core.annotation.Logger
import com.example.core.annotation.Logger.Companion.log
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Logger
class FacebookWebhooksController {


    @PostMapping("facebook/webhook/page")
    fun receivePageWebhook(
        @RequestBody eventContent: String
    ) {
        log.info(eventContent)
    }
}