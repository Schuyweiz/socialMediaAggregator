package com.example.core.user.dao

import com.example.core.model.User
import com.example.core.user.repository.UserRepository
import com.example.core.utils.Logger.Companion.log
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserDetailsServiceImpl(
    private val userRepository: UserRepository,
): UserDetailsService {

    @Transactional(readOnly = true)
    override fun loadUserByUsername(email: String?): User {
        log.info("Trying to load user from database for authnetication")
        if (email == null)
            throw Exception("Username not provided the loadUserByUsername from UserDetailsService")

        return userRepository.findByEmail(email)?.apply { authorities = mutableListOf(SimpleGrantedAuthority("ROLE_USER")) }
            ?: throw UsernameNotFoundException("User with the email $email not found.")
    }


}