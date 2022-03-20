package com.example.auth.app.registration

import com.example.core.model.User
import org.springframework.context.ApplicationEvent


class OnRegistrationCompleteEvent(
    user: User,
) : ApplicationEvent(user) {
}