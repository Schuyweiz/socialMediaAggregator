package com.example.socialmediaaggregator.registration.validation

import javax.validation.Constraint
import kotlin.annotation.AnnotationTarget.*
import kotlin.reflect.KClass

@Target(allowedTargets = [TYPE, FIELD, ANNOTATION_CLASS])
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [EmailValidator::class])
annotation class ValidEmail(
    val message: String = "Invalid email",
    val groups: Array<KClass<out Any>> = [],
    val payload: Array<KClass<out Any>> = [],
)
