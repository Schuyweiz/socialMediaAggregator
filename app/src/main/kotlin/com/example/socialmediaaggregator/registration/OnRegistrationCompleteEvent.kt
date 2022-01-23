package com.example.socialmediaaggregator.registration

import com.example.socialmediaaggregator.registration.model.User
import org.springframework.context.ApplicationEvent


class OnRegistrationCompleteEvent(
    private val user: User,
) : ApplicationEvent(user) {
}