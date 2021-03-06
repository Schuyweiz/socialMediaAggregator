package com.example.core.service.impl

import com.example.core.exceptions.TokenExpiredException
import com.example.core.exceptions.TokenNotFoundException
import com.example.core.exceptions.UserAlreadyExistsException
import com.example.core.model.User
import com.example.core.dto.UserDto
import com.example.core.model.VerificationToken
import com.example.core.model.calculateExpiryDate
import com.example.core.repository.TokenRepository
import com.example.core.repository.UserRepository
import com.example.core.annotation.Logger
import com.example.core.annotation.Logger.Companion.log
import com.example.core.mapper.UserMapper
import com.example.core.service.UserService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime


@Logger
@Service
class UserServiceImpl(
    val userRepository: UserRepository,
    val tokenRepository: TokenRepository,
    val userMapper: UserMapper,
    val encoder: PasswordEncoder,
) : UserService {

    @Transactional
    override fun saveUser(userDto: UserDto): User {
        log.info("Verifying user with email ${userDto.email} exists.")

        userRepository.findByEmail(userDto.email)?.apply {
            throw UserAlreadyExistsException("User with email ${userDto.email} already exists.")
        }

        log.info("Saving a user with an email ${userDto.email}")
        val encodedPassword = encoder.encode(userDto.password)
        val user = userMapper.userDtoToUser(userDto, encodedPassword)

        return userRepository.save(user)
    }

    @Transactional
    override fun createVerificationToken(user: User, token: String) = tokenRepository.save(
        VerificationToken(
            token = token,
            user = user,
            expiryDate = calculateExpiryDate(expirationTimeMinutes)
        )
    )

    @Transactional
    override fun enableUser(token: String) {
        val verificationToken = getValidToken(token)
        val user = verificationToken.user

        user.enabled = true
        userRepository.save(user)
    }

    @Transactional(readOnly = true)
    override fun getValidToken(token: String): VerificationToken {
        log.info("Verifying token validity.")

        val verificationToken =
            tokenRepository.getByToken(token) ?: throw TokenNotFoundException("Token $token not found.")

        if (verificationToken.expiryDate.minusSeconds(LocalDateTime.now().second.toLong()).second <= 0)
            throw TokenExpiredException("Token $token expired.")

        return verificationToken
    }

    companion object {
        //TODO: maybe should replace with value injection
        const val expirationTimeMinutes = 60 * 24
    }
}