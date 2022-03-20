package com.example.socialmediaaggregator

import com.example.core.model.SocialMediaToken
import com.example.core.model.SocialMediaType
import com.example.core.model.User
import com.example.core.user.repository.SocialMediaTokenRepository
import com.example.core.user.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.function.Consumer


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
        socialMediaTokenRepository: SocialMediaTokenRepository,
        encoder: PasswordEncoder
    ): CommandLineRunner? {
        return CommandLineRunner {
            val user = User(
                firstName = "kirill", lastName = "pike",
                userPassword = encoder.encode("1234"), email = "kpike@mail.ru", enabled = true
            )
            val token =
                "EAAHO4difc4UBAIF1jNFfWNu7HZAYpzvq0L1AHhkuuN0gkGrHRI6Qa9yGHHK9mw9nI4pygqQ8EtXd6scF2hwVMy8uAWvE3kmmmItHYVPEJSTGaexE824AwXiKVpKL2CqmtZCSg55ZCa9cLJf8TtuQiYcHV8wj208eLTlwZAqULPDuiZBPYQUoU";
            val socialMediaTokens =
                listOf(SocialMediaToken(token = token, socialMediaType = SocialMediaType.FACEBOOK, user = user))
            userRepository.save(user)
            socialMediaTokenRepository.save(socialMediaTokens[0])
        }
    }
}
