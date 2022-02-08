package com.example.auth.app.registration

import com.example.core.user.dao.UserService
import com.example.core.user.model.UserDto
import com.example.core.utils.Logger
import com.example.core.utils.Logger.Companion.log
import org.springframework.context.ApplicationEventPublisher
import org.springframework.web.bind.annotation.*

@Logger
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
        log.info("User saved successfully, pending an email to verify the account.")

        eventPublisher.publishEvent(OnRegistrationCompleteEvent(user))
    }

    @GetMapping("/register/confirm")
    fun confirmRegistration(
        @RequestParam("token") token: String,
    ) {
        userService.enableUser(token)
    }
}