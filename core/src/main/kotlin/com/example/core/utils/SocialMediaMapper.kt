package com.example.core.utils

import com.example.core.model.SocialMedia
import com.example.core.model.socialmedia.SocialMediaDto
import org.springframework.stereotype.Component

@Component
class SocialMediaMapper {

    fun mapToDto(socialMedia: SocialMedia) =
        SocialMediaDto(socialMedia.nativeId, socialMedia.token, socialMedia.socialMediaType)
}