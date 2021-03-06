package com.example.auth.app.registration

import com.example.core.service.UserService
import com.example.core.model.User
import com.example.core.annotation.Logger
import com.example.core.annotation.Logger.Companion.log
import org.springframework.context.ApplicationListener
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component
import java.util.*

@Logger
@Component
class RegistrationListener(
    private val userService: UserService,
    val mailSender: JavaMailSender,
) : ApplicationListener<OnRegistrationCompleteEvent> {

    override fun onApplicationEvent(event: OnRegistrationCompleteEvent) = sendConfirmationEmail(event)

    private fun sendConfirmationEmail(event: OnRegistrationCompleteEvent) {
        val user = event.source as User
        val token = UUID.randomUUID().toString()

        //todo: do  i need tokens here? probably not
        log.info("Creating a verification token for the user with an email ${user.email}")
        userService.createVerificationToken(user, token)

        //fixme: string link should not be hand written
        log.info("Activate account through the link http://localhost:8443/register/confirm?token=$token")
        //TODO: uncomment when mail sender is fully configured
//
//        val email = SimpleMailMessage().apply {
//            this.setTo(user.mail)
//            this.setSubject("Registration verification")
//            this.setText("http://localhost:8080/register/confirm?token=$token")
//        }
//        log.info("Sending an email to the address ${user.mail}")
//        mailSender.send(email)
    }
}