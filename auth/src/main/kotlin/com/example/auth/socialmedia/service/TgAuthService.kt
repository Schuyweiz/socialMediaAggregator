package com.example.auth.socialmedia.service

import com.example.core.dto.SocialMediaDto
import com.example.core.model.*
import com.example.core.model.socialmedia.SocialMediaType
import com.example.core.repository.SocialMediaRepository
import com.example.core.service.impl.UserQueryService
import it.tdlight.common.internal.InternalClient
import it.tdlight.tdlight.ClientManager
import org.springframework.stereotype.Service
import java.util.concurrent.CountDownLatch

@Service
class TgAuthService(
    private val userQueryService: UserQueryService,
    private val socialMediaRepository: SocialMediaRepository,
) {

    fun authPhoneNumber(number: String, userId: Long): SocialMediaDto {
        val countDownLatch = CountDownLatch(1)

        val user = userQueryService.findByIdOrThrow(userId)
        val tgClient =
            TgClient(countDownLatch = countDownLatch, client = TgClientFactory.createClient()).apply { init() }
        countDownLatch.await()


        val temp = tgClient.client as? InternalClient
        val clientid = temp?.clientId


        tgClient.countDownLatch = CountDownLatch(1)
        tgClient.authPhoneNumber(number)
        countDownLatch.await()

        if (tgClient.error.isNullOrEmpty()) {
            val socialMedia = SocialMedia(
                nativeId = clientid?.toLong(),
                token = tgClient.token,
                socialMediaType = SocialMediaType.TG,
                user = user
            )

            return socialMediaRepository.save(socialMedia).toDto()
        } else {
            throw Exception("Something went wrong ${tgClient.error}")
        }
    }

    fun authCode(code: String, userId: Long, accountId: Long) {
        val countDownLatch = CountDownLatch(1)
        val user = userQueryService.findByIdOrThrow(userId)
        val token = user.socialMediaSet.first { it.id == accountId }.token

        val tgClient =
            TgClient(countDownLatch = countDownLatch, client = ClientManager.create(), token = token).apply { init() }
        countDownLatch.await()

        tgClient.countDownLatch = CountDownLatch(1)
        tgClient.authCode(code)
        countDownLatch.await()

        if (!tgClient.error.isNullOrEmpty()) {
            throw Exception("Something went wrong ${tgClient.error}")
        }
    }
}