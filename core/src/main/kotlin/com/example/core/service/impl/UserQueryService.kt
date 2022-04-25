package com.example.core.service.impl

import com.example.core.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserQueryService(
    private val userRepository: UserRepository,
) {
    fun findByIdOrThrow(id: Long) = userRepository.findById(id).orElseThrow()
}