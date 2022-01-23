package com.example.socialmediaaggregator.registration.validation

import java.util.regex.Pattern
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class EmailValidator : ConstraintValidator<ValidEmail, String> {

    override fun initialize(constraintAnnotation: ValidEmail?) {
        //
    }

    override fun isValid(value: String?, context: ConstraintValidatorContext?): Boolean =
        value?.let { validateEmail(it) } ?: false

    private fun validateEmail(email: String): Boolean =
        Pattern
            .compile(EMAIL_PATTERN)
            .matcher(email)
            .matches()


    companion object {
        const val EMAIL_PATTERN: String =
            """^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$"""
    }
}