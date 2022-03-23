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
                "EAAHO4difc4UBAPeRcPMSZC54dEOUBAEtitiljPNGhZC9FXBS0m77mhrfGMuvOoAIpXaUslVZA3U3IDWnDM2bBwc8SgR5HQXb1TniZATJlPpGdhAAtqRAFwASHu0nPmhkoZAh0ZCZAEZCicpUSoYd0g0bvWu4TLKAeKCA5WVmMR7wIizjK9To6ZBFSAnfvrg7vTkMZD";
            val token2 = "EAAHO4difc4UBAPeRcPMSZC54dEOUBAEtitiljPNGhZC9FXBS0m77mhrfGMuvOoAIpXaUslVZA3U3IDWnDM2bBwc8SgR5HQXb1TniZATJlPpGdhAAtqRAFwASHu0nPmhkoZAh0ZCZAEZCicpUSoYd0g0bvWu4TLKAeKCA5WVmMR7wIizjK9To6ZBFSAnfvrg7vTkMZD"
            val socialMedia =
                mutableSetOf(SocialMedia(token = token, socialMediaType = SocialMediaType.FACEBOOK_PAGE, user = user, nativeId = 110351558278776),
                SocialMedia(token = token2, socialMediaType = SocialMediaType.FACEBOOK_USER, user = user, nativeId = null)
                )
            user.socialMediaSet = socialMedia
            userRepository.save(user)
        }
    }
}
