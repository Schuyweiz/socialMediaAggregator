package com.example.socialmediaaggregator.registration

import com.example.socialmediaaggregator.registration.exception.TokenExpiredException
import com.example.socialmediaaggregator.registration.exception.TokenNotFoundException
import com.example.socialmediaaggregator.registration.exception.UserAlreadyExistsException
import com.example.socialmediaaggregator.registration.model.User
import com.example.socialmediaaggregator.registration.model.UserDto
import com.example.socialmediaaggregator.registration.model.VerificationToken
import com.example.socialmediaaggregator.registration.model.calculateExpiryDate
import com.example.socialmediaaggregator.registration.repository.TokenRepository
import com.example.socialmediaaggregator.registration.repository.UserRepository
import com.example.socialmediaaggregator.utils.UserMapper
import com.example.socialmediaaggregator.utils.Logger
import com.example.socialmediaaggregator.utils.Logger.Companion.log
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime


@Logger
@Service
class UserService(
    val userRepository: UserRepository,
    val tokenRepository: TokenRepository,
    val userMapper: UserMapper,
) : UserDetailsService {

    override fun loadUserByUsername(email: String?): UserDetails {
        if (email == null)
            throw Exception("Username not provided the loadUserByUsername from UserDetailsService")

        val user = userRepository.findByMail(email)
            ?: throw UsernameNotFoundException("User with the email $email not found.")

        return org.springframework.security.core.userdetails.User(
            user.mail,
            user.passwordHash,
            user.enabled,
            true,
            true,
            true,
            //TODO: create a proper set of authorities and how to extraact them from the user
            mutableListOf(SimpleGrantedAuthority("ROLE_USER"))
        )
    }

    @Transactional
    fun saveUser(userDto: UserDto): User {
        log.info("Verifying user with email ${userDto.mail} exists.")

        if (userExists(userDto))
            throw UserAlreadyExistsException("User with email ${userDto.mail} already exists.")

        log.info("Saving a user with an email ${userDto.mail}")
        val user = userMapper.userDtoToUser(userDto)
        return userRepository.save(user)
    }

    @Transactional(readOnly = true)
    fun userExists(userDto: UserDto) = userRepository.findByMail(userDto.mail) != null

    @Transactional
    fun createVerificationToken(user: User, token: String) = tokenRepository.save(
        VerificationToken(
            id = null,
            token = token,
            user = user,
            expiryDate = calculateExpiryDate(expirationTimeMinutes)
        )
    )

    @Transactional
    fun enableUser(token: String) {
        log.info("Verifying token validity.")
        val verificationToken = getValidToken(token)
        val user = verificationToken.user
        user.enabled = true
        userRepository.save(user)
    }

    @Transactional(readOnly = true)
    fun getValidToken(token: String): VerificationToken {
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