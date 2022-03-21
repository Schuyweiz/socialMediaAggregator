package com.example.socialmediaaggregator

import com.example.core.model.SocialMedia
import com.example.core.model.SocialMediaType
import com.example.core.model.User
import com.example.core.user.repository.SocialMediaRepository
import com.example.core.user.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.security.crypto.password.PasswordEncoder


@SpringBootApplication(scanBasePackages = ["com.example.socialmediaaggregator", "com.example.core", "com.example.auth", "com.example.api"])
@EnableJpaRepositories(basePackages = ["com.example.core"])
@EntityScan(basePackages = ["com.example.core"])
class SocialMediaAggregatorApplication {


    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<SocialMediaAggregatorApplication>(*args)
        }
    }

    @Bean
    fun init(
        userRepository: UserRepository,
        socialMediaTokenRepository: SocialMediaRepository,
        encoder: PasswordEncoder
    ): CommandLineRunner? {
        return CommandLineRunner {
            val user = User(
                firstName = "kirill", lastName = "pike",
                userPassword = encoder.encode("1234"), email = "kpike@mail.ru", enabled = true
            )
            val token =
                "EAAHO4difc4UBADZCZBFrZA2tzbsM89oOJdYL3ViWKS4m7ZA7YR6gF9ZA3oyMhuMavPCsxgTN9XEQobCRaBlI61PvmQdlpWT7wfBEx6XKdwI7f4gtpt3QF9e6W3ewCY1HbyQZAQr3bYs02CLNbUY0CdckT7eYElzs6Q3TYNPaN5xoY63c3F5dBY";
            val socialMedia =
                listOf(SocialMedia(token = token, socialMediaType = SocialMediaType.FACEBOOK_USER, user = user, nativeId = 110351558278776))
            userRepository.save(user)
            socialMediaTokenRepository.save(socialMedia[0])
        }
    }
}
