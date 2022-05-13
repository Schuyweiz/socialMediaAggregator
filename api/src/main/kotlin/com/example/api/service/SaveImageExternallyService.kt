package com.example.api.service

import com.example.api.dto.SaveImageResponseDto
import com.example.core.annotation.Logger
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
@Logger
class SaveImageExternallyService(
    private val restTemplate: RestTemplate,
) {

    fun saveImage(multipartFile: MultipartFile): String? {
        val encodedFile = Base64.getEncoder().encodeToString(multipartFile.bytes)
        val headers = HttpHeaders()
        headers.contentType = MediaType.MULTIPART_FORM_DATA
        val map: MultiValueMap<String, Any> = LinkedMultiValueMap()
        //TODO; move key to a secret place
        //TODO expiration timer
        map.add("key", "4327e786b78f0e400eb1f07a6be462d1")
        map.add("image", encodedFile)
        val request: HttpEntity<MultiValueMap<String, Any>> = HttpEntity(map, headers)

        val response = restTemplate.postForEntity(
            "https://api.imgbb.com/1/upload",
            request,
            SaveImageResponseDto::class.java
        )

        return response.body?.data?.display_url
    }

    fun saveImage(bytes: ByteArray): String? {
        val encodedFile = Base64.getEncoder().encodeToString(bytes)
        val headers = HttpHeaders()
        headers.contentType = MediaType.MULTIPART_FORM_DATA
        val map: MultiValueMap<String, Any> = LinkedMultiValueMap()
        //TODO; move key to a secret place
        //TODO expiration timer
        map.add("key", "4327e786b78f0e400eb1f07a6be462d1")
        map.add("image", encodedFile)
        val request: HttpEntity<MultiValueMap<String, Any>> = HttpEntity(map, headers)

        val response = restTemplate.postForEntity(
            "https://api.imgbb.com/1/upload",
            request,
            SaveImageResponseDto::class.java
        )

        return response.body?.data?.display_url
    }
}