package com.example.core.libs.pinterest

import com.example.core.libs.pinterest.model.PinterestAuthneticationData
import com.nimbusds.jose.util.Base64
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

class PinterestAuthClient(
) {

    private val authTemplate = RestTemplateBuilder().rootUri("https://www.pinterest.com/").build()


    private val pinterestAuthUri = "https://www.pinterest.com/oauth/"


    fun getAuthneticationUrl(clientId: Long, redirectUri: String): String = getAuthMap(
        clientId,
        redirectUri,
        "code",
        scope = "ads:read,boards:read,boards:write,pins:read,pins:write,user_accounts:read"
    ).entries.joinToString(
        separator = "&",
        prefix = """"$pinterestAuthUri?""",
        transform = { entry -> """${entry.key}=${entry.value}""" })

    fun exchangeCodeForToken(
        code: String,
        redirectUri: String,
        clientId: Long,
        clientSecret: String
    ): PinterestAuthneticationData? {
        val formMap = linkedMapOf("grant_type" to "authorization_code", "code" to code, "redirect_uri" to redirectUri)
        val authHeader = Base64.encode("""$clientId:$clientSecret""").toString()
        val headers = HttpHeaders().apply {
            this.setBasicAuth(authHeader)
            this.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        }

        val requestEntity = HttpEntity(formMap, headers)
        //todo: error handling
        return authTemplate.postForEntity(
            "oauth/form",
            requestEntity,
            PinterestAuthneticationData::class.java
        ).body
    }


    private fun getAuthMap(clientId: Long, redirectUri: String, responseType: String, scope: String) = hashMapOf(
        "client_id" to clientId.toString(),
        "redirect_uri" to redirectUri,
        "response_type" to responseType,
        "scope" to scope
    )
}