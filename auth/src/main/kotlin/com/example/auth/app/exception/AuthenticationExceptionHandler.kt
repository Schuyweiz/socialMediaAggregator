package com.example.auth.app.exception

import com.example.core.utils.Logger
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@Logger
@ControllerAdvice
class AuthenticationExceptionHandler : ResponseEntityExceptionHandler() {


}