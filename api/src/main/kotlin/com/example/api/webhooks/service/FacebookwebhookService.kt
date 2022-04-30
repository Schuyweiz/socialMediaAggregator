package com.example.api.webhooks.service

import com.restfb.DefaultJsonMapper
import com.restfb.types.webhook.WebhookObject
import com.restfb.webhook.Webhook
import org.springframework.stereotype.Service

@Service
class FacebookwebhookService(
    private val facebookWebhook: Webhook,
    private val defaultFacebookMapper: DefaultJsonMapper
) {

    fun processMessageWebhook(jsonObject: String) {
        val jsonobj = defaultFacebookMapper.toJavaObject(
            jsonObject, WebhookObject::class.java
        )
        val temp = DefaultJsonMapper().toJavaObject(jsonObject, WebhookObject::class.java)
        facebookWebhook.process(jsonobj)
    }
}