package com.example.core.utils

import com.example.core.user.model.User
import com.example.core.user.model.UserDto
import com.nhaarman.mockito_kotlin.whenever
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.crypto.password.PasswordEncoder

@ExtendWith(MockitoExtension::class)
internal class UserMapperTest {

    @Mock
    lateinit var passwordEncoder: PasswordEncoder

    private lateinit var userMapper: UserMapper

    @BeforeEach
    fun init() {
        userMapper = UserMapper(passwordEncoder)
        whenever(passwordEncoder.encode(anyString())).thenReturn("encoded")
    }

    @Test
    fun `verify userDto to user mapper works correctly`() {
        val userDto = UserDto(
            firstName = "first name",
            secondName = "second name",
            mail = "mail@mail.ru",
            username = "username",
            password = "password",
        )

        val expectedUser = User(
            id = null,
            firstName = "first name",
            secondName = "second name",
            mail = "mail@mail.ru",
            username = "username",
            passwordHash = "encoded",
            enabled = false,
        )

        val actualUser = userMapper.userDtoToUser(userDto)

        expectedUser shouldBe actualUser
    }
}