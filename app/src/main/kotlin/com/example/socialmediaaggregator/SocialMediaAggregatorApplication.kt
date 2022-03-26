package com.example.socialmediaaggregator

import com.example.core.model.SocialMedia
import com.example.core.model.SocialMediaType
import com.example.core.model.User
import com.example.core.repository.SocialMediaRepository
import com.example.core.repository.UserRepository
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
            val pageToken =
                "EAAHO4difc4UBADrba2mvv7GxnebpJZB6BfORsmXBXZC6TBYNkKtxNlOF6K0GfmMsOsAeBhoUvoNpiBCayrktA8grR8mYxyBR4hQivaymW7KVCkDDSfEIwk8W5kjI7fDbdukVCYLpKjMfmoiAfpcVpO5YBABTQc66q2hsUTW2qZCHlONHk5gk9hev47ZAa7cZD";
            val userToken = "EAAHO4difc4UBAPWLiSSJLPt4wSqVxT7542ewQDR0ZAJMXMsLV5HZBjjQeP8Y1nTj9FOhfqFtzWY3nn5nCHXBss3IiZC9WIhocLvKCVFoXlnQBfNP6N2mop7A4HbKP7MwNibigIsjyivH8PULDFdMiDY9oX55piHdTBStSuQxoZBkDxcYsMew"
            val socialMedia =
                mutableSetOf(SocialMedia(token = pageToken, socialMediaType = SocialMediaType.FACEBOOK_PAGE, user = user, nativeId = 110351558278776),
                SocialMedia(token = userToken, socialMediaType = SocialMediaType.FACEBOOK_USER, user = user, nativeId = null)
                )
            user.socialMediaSet = socialMedia
            userRepository.save(user)
        }
    }
}
