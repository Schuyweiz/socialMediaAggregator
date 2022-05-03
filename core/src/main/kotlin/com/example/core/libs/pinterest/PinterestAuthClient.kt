package com.example.core.libs.pinterest

import com.example.core.libs.pinterest.model.PinterestAuthneticationData
import com.nimbusds.jose.util.Base64
import org.apache.http.impl.client.BasicCookieStore
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.impl.client.LaxRedirectStrategy
import org.apache.http.impl.cookie.BasicClientCookie
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.util.LinkedMultiValueMap
import java.time.Duration
import javax.servlet.http.Cookie

class PinterestAuthClient(
) {

    private val authTemplate = RestTemplateBuilder().rootUri("https://www.pinterest.com/")
        .setConnectTimeout(Duration.ofSeconds(6))
        .setReadTimeout(Duration.ofSeconds(6))
        .build()

    private val tokenTemplate = RestTemplateBuilder().rootUri("https://api.pinterest.com/")
        .setConnectTimeout(Duration.ofSeconds(6))
        .setReadTimeout(Duration.ofSeconds(6))
        .build()

    private val pinterestAuthUri = "https://www.pinterest.com/oauth/"
    private val tokenAuthU = "https://www.pinterest.com/"


    fun getAuthneticationUrl(clientId: String, redirectUri: String): String = getAuthMap(
        clientId,
        redirectUri,
        "code",
        scope = "ads:read,boards:read,boards:write,pins:read,pins:write,user_accounts:read"
    ).entries.joinToString(
        separator = "&",
        prefix = """$pinterestAuthUri?""",
        transform = { entry -> """${entry.key}=${entry.value}""" })

    fun exchangeCodeForToken(
        code: String,
        redirectUri: String,
        clientId: String,
        clientSecret: String,
    ): PinterestAuthneticationData? {
        val formMap = LinkedMultiValueMap<String, Any?>()
        linkedMapOf("grant_type" to "authorization_code", "code" to code, "redirect_uri" to redirectUri).forEach {
            formMap.add(it.key, it.value)
        }
        val authHeader = Base64.encode("""$clientId:$clientSecret""").toString()
        val headers = HttpHeaders().apply {
            this.setBasicAuth(authHeader)
            this.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        }

        val requestEntity = HttpEntity(formMap, headers)

//        authTemplate.requestFactory = getFactory()
        val response = tokenTemplate.postForEntity(
            "/v5/oauth/token",
            requestEntity,
            PinterestAuthneticationData::class.java
        )

        return response.body
    }

    private fun getFactory(): HttpComponentsClientHttpRequestFactory {
        val factory = HttpComponentsClientHttpRequestFactory()
        val client = HttpClientBuilder.create().setRedirectStrategy(LaxRedirectStrategy())
            .build()
        factory.httpClient = client
        return factory
    }


    private fun getAuthMap(clientId: String, redirectUri: String, responseType: String, scope: String) = hashMapOf(
        "client_id" to clientId,
        "redirect_uri" to redirectUri,
        "response_type" to responseType,
        "scope" to scope
    )
}