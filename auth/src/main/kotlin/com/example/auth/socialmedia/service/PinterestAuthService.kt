package com.example.auth.socialmedia.service

import com.example.core.dto.SocialMediaDto
import com.example.core.libs.pinterest.PinterestAuthClient
import com.example.core.mapper.SocialMediaMapper
import com.example.core.model.SocialMedia
import com.example.core.model.socialmedia.SocialMediaType
import com.example.core.repository.SocialMediaRepository
import com.example.core.service.impl.UserQueryService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PinterestAuthService(
    @Value("\${app.pinterest.app-id}")
    private val appId: String,
    @Value("\${app.pinterest.app-secret}")
    private val appSecret: String,
    @Value("\${app.pinterest.redirect-uri}")
    private val redirectUrl: String,
    private val userQueryService: UserQueryService,
    private val socialMedaRepository: SocialMediaRepository,
    private val socialMediaMapper: SocialMediaMapper,
) {

    fun getAuthUri() = PinterestAuthClient().getAuthneticationUrl(appId, redirectUrl)

    @Transactional
    fun exchangeCodeForToken(
        nativeId: Long,
        userId: Long,
        code: String,
    ): SocialMediaDto? =
        PinterestAuthClient().exchangeCodeForToken(code, redirectUrl, appId, appSecret)?.let {
            val user = userQueryService.findByIdOrThrow(userId)
            val account = socialMedaRepository.save(
                SocialMedia(
                    nativeId = nativeId,
                    token = it.access_token,
                    socialMediaType = SocialMediaType.PINTEREST,
                    user = user
                )
            )
            socialMediaMapper.mapToDto(account)
        }
}