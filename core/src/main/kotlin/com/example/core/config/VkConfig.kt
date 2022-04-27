package com.example.core.config

import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.httpclient.HttpTransportClient
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class VkConfig {

    @Bean
    fun vkClient() = VkApiClient(HttpTransportClient.getInstance())
}