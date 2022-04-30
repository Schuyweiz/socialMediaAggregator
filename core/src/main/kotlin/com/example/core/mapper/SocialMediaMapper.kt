package com.example.core.mapper

import com.example.core.dto.PageAuthenticateDto
import com.example.core.model.SocialMedia
import com.example.core.dto.SocialMediaDto
import com.restfb.types.Page
import org.springframework.stereotype.Component

@Component
class SocialMediaMapper {

    fun mapToDto(socialMedia: SocialMedia) =
        SocialMediaDto(
            id = socialMedia.id,
            nativeId = socialMedia.nativeId,
            token = socialMedia.token,
            socialMedia.socialMediaType
        )

    fun mapAuthenticationDto(page: Page) =
        PageAuthenticateDto(
            id = page.id.toLong(),
            token = page.accessToken,
            name = page.name,
            instagramId = page.instagramBusinessAccount?.id?.toLong()
        )
}