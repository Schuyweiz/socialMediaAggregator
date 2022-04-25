package com.example.core.service.impl

import com.example.core.model.SocialMedia
import com.example.core.repository.SocialMediaRepository
import net.bytebuddy.implementation.bytecode.Throw
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class SocialMediaQueryService(
    private val socialMediaRepository: SocialMediaRepository,
) {

    fun findByIdOrThrow(id: Long): SocialMedia = socialMediaRepository.findByIdOrNull(id)?: throw Exception("")
}