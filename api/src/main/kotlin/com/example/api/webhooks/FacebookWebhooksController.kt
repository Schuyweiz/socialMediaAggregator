package com.example.api.webhooks

import com.example.api.webhooks.FacebookwebhookService
import com.example.core.annotation.Logger
import com.example.core.annotation.Logger.Companion.log
import com.example.core.model.socialmedia.Post
import com.example.core.repository.PostRepository
import com.example.core.repository.SocialMediaRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Logger
class FacebookWebhooksController(
    private val facebookwebhookService: FacebookwebhookService,
    private val postRepository: PostRepository,
    private val socialMediaRepository: SocialMediaRepository,
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

    @GetMapping("test")
    fun test() {
        val socialMedia = socialMediaRepository.findById(1L).orElseThrow()
        postRepository.save(
            Post(
                id = 0,
                nativeId = "123",
                textContent = "sdfg",
                socialMedia = socialMedia,
                likes = 0,
                comments = listOf()
            )
        )
    }
}