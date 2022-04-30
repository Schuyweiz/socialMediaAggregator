package com.example.auth.socialmedia.service

import com.example.core.model.SocialMedia
import com.example.core.model.SocialMediaType
import com.example.core.service.VkApi
import com.example.core.service.impl.UserQueryService
import com.vk.api.sdk.client.VkApiClient
import com.vk.api.sdk.objects.GroupAuthResponse
import com.vk.api.sdk.objects.groups.Fields
import com.vk.api.sdk.objects.groups.Filter
import com.vk.api.sdk.objects.groups.responses.GetResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class VkAuthService(
    private val vkApiClient: VkApiClient,
    @Value("\${app.vk.app-id}")
    private val appCode: Int,
    @Value("\${app.vk.app-secret}")
    private val appSecret: String,
    @Value("\${app.vk.auth.redirect-url}")
    private val redirectUrl: String,
    private val userQueryService: UserQueryService,
) : VkApi {
    private val map: Map<String, String> = mapOf(
        "client_id" to appCode.toString(),
        "redirect_uri" to redirectUrl,
        "display" to "page,wall",
        "scope" to "groups",
        "response_type" to "code",
        "state" to "somestateLALALA"
    )


    //TODO: версию апи неплохо бы с сайта подтяшивать и обновлять
    private fun getGroupMap(groupIds: List<Int>): Map<String, String> = mapOf(
        "client_id" to appCode.toString(),
        "redirect_uri" to redirectUrl,
        "group_ids" to groupIds.joinToString(","),
        "display" to "page",
        "scope" to "photos,messages,manage",
        "response_type" to "code",
        "v" to "5.131",
        "state" to "somestateLALALA"
    )

    private fun getPath(params: Map<String, String>) = "https://oauth.vk.com/authorize" + params.entries.joinToString(
        "&",
        "?",
        "",
        transform = { entry -> """${entry.key}=${entry.value}""" })

    fun getAuthCodeLink(): String = getPath(map)


    @Transactional
    fun authUser(code: String, userId: Long): Any =
        vkApiClient.oAuth().userAuthorizationCodeFlow(appCode, appSecret, redirectUrl, code).execute().apply {
            if (this.error.isNullOrEmpty()) {
                addAccountOfTypeToUser(userId, this.userId.toLong(), this.accessToken, SocialMediaType.VK_USER)
            } else {
                throw Exception("An error occured ${this.error}")
            }
        }

    @Transactional
    fun authGroups(code: String, userId: Long): GroupAuthResponse =
        vkApiClient.oAuth().groupAuthorizationCodeFlow(appCode, appSecret, redirectUrl, code).execute().apply {
            if (this.error.isNullOrEmpty()) {
                this.accessTokens.entries.forEach {
                    addAccountOfTypeToUser(userId, it.key.toLong(), it.value, SocialMediaType.VK_GROUP)
                }
            } else {
                throw Exception("An error occurred while authenticating vk groups $error")
            }
        }

    @Transactional
    fun getUserGroups(userId: Long, accountId: Long): GetResponse =
        userQueryService.findByIdOrThrow(userId).socialMediaSet.first { it.id == accountId }.let {
            return@let vkApiClient.groups().get(getUserClient(it.nativeId, it.token)).userId(it.nativeId?.toInt())
                .filter(Filter.ADMIN).fields(Fields.CAN_MESSAGE, Fields.CAN_POST).execute()
        }

    fun getGroupAuthLink(groupIds: List<Int>) = getPath(getGroupMap(groupIds))

    private fun addAccountOfTypeToUser(userId: Long, nativeId: Long, token: String, type: SocialMediaType) {
        val user = userQueryService.findByIdOrThrow(userId)
        user.socialMediaSet.add(
            SocialMedia(
                nativeId = nativeId,
                token = token,
                socialMediaType = type,
                user = user
            )
        )
    }

}