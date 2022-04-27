package com.example.core.service

import com.vk.api.sdk.client.actors.GroupActor
import com.vk.api.sdk.client.actors.UserActor

interface VkApi {

    fun getUserClient(vkId: Long?, token: String) = UserActor(vkId?.toInt(), token)

    fun getGroupClient(vkId: String, token: String) = GroupActor(vkId.toInt(), token)
}