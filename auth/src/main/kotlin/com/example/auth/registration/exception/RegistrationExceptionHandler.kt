package com.example.auth.registration.exception

import com.example.core.user.exceptions.UserAlreadyExistsException
import com.example.core.utils.Logger
import com.example.core.utils.Logger.Companion.log
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler


@ControllerAdvice
@Logger
class RegistrationExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleUserAlreadyRegistered(
        exception: UserAlreadyExistsException, request: WebRequest
    ): ResponseEntity<Any> {
        //TODO: inject message as a VALUE
        log.debug(exception.message, exception)
        return ResponseEntity(exception.message, HttpStatus.CONFLICT)
    }
}