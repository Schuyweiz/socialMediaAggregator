package com.example.auth.registration

import com.example.core.user.dao.UserService
import com.example.core.user.model.UserDto
import org.springframework.context.ApplicationEventPublisher
import org.springframework.web.bind.annotation.*

@RestController
class RegistrationController(
    val userService: UserService,
    val eventPublisher: ApplicationEventPublisher,
) {

    @PostMapping("/register")
    fun registerNewUser(
        @RequestBody(required = true) userDto: UserDto
    ) {
        val user = userService.saveUser(userDto)
        eventPublisher.publishEvent(OnRegistrationCompleteEvent(user))
    }

    @GetMapping("/register/confirm")
    fun confirmRegistration(
        @RequestParam("token") token: String,
    ) {
        userService.enableUser(token)
    }
}