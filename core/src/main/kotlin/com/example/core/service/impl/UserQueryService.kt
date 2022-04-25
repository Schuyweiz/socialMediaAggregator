package com.example.core.service.impl

import com.example.core.model.User
import com.example.core.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserQueryService(
    private val userRepository: UserRepository,
) {
    fun findByIdOrThrow(id: Long): User = userRepository.findByIdOrNull(id) ?: throw Exception("entityNotFound $id")
}