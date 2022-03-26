package com.example.core.mapper

import com.example.core.model.SocialMedia
import com.example.core.dto.SocialMediaDto
import org.springframework.stereotype.Component

@Component
class SocialMediaMapper {

    fun mapToDto(socialMedia: SocialMedia) =
        SocialMediaDto(socialMedia.nativeId, socialMedia.token, socialMedia.socialMediaType)
}