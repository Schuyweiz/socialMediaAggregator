package com.example.core.service.impl

import com.example.core.repository.SocialMediaRepository
import org.springframework.stereotype.Service

@Service
class SocialMediaQueryService(
    private val socialMediaRepository: SocialMediaRepository,
) {

    fun findByIdOrThrow(id: Long) = socialMediaRepository.findById(id).orElseThrow()
}