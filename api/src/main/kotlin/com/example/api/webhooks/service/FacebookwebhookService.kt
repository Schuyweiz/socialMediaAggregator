package com.example.api.webhooks.service

import com.restfb.DefaultJsonMapper
import com.restfb.types.webhook.WebhookObject
import com.restfb.webhook.Webhook
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FacebookwebhookService(
    private val facebookWebhook: Webhook,
    private val defaultFacebookMapper: DefaultJsonMapper
) {

    @Transactional
    fun processMessageWebhook(jsonObject: String) {
        val jsonobj = defaultFacebookMapper.toJavaObject(
            jsonObject, WebhookObject::class.java
        )
        //todo: based on pbject type
        facebookWebhook.process(jsonobj)
    }
}