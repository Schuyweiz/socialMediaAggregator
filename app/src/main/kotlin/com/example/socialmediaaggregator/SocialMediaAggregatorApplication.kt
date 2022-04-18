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
                "EAAHO4difc4UBAFncunwJUqjDl3N8EsS8DLF9h0TfFKAuajGA2tOZAB9bQFP6V6jIJK2W2BG16pZCtFiVIXSr4uT4sEyGmME0MJ140x0m83qxYkS2ZBnQN1fbban6SiOitHkyHaZCFOvXWUgOXpnUnNjGf7zphtBeKuFoXyIAjDZAHx4WdZBSWZBKFwNu0439RYZD";
            val userToken =
                "EAAHO4difc4UBAPWLiSSJLPt4wSqVxT7542ewQDR0ZAJMXMsLV5HZBjjQeP8Y1nTj9FOhfqFtzWY3nn5nCHXBss3IiZC9WIhocLvKCVFoXlnQBfNP6N2mop7A4HbKP7MwNibigIsjyivH8PULDFdMiDY9oX55piHdTBStSuQxoZBkDxcYsMew"
            val socialMedia =
                mutableSetOf(
                    SocialMedia(
                        token = pageToken,
                        socialMediaType = SocialMediaType.FACEBOOK_PAGE,
                        user = user,
                        nativeId = 110351558278776
                    ),
                    SocialMedia(
                        token = userToken,
                        socialMediaType = SocialMediaType.FACEBOOK_USER,
                        user = user,
                        nativeId = null
                    )
                )
            user.socialMediaSet = socialMedia
            userRepository.save(user)
            val user2 = User(
                firstName = "kirill", lastName = "pike",
                userPassword = encoder.encode("1234"), email = "pikek@mail.ru", enabled = true
            )
            val user2token =
                "EAAHO4difc4UBAHj3SDtCBm3y3cxrSz19o9DknpOu9UcQKTwVFgzqFuJaaZCt2CX0Xpkd7dwgBZCe9Jihca8HfHRWD8jeaj3mjZAt2wq8602YZCHAec4H013lmcMlB80hjMT5m4ngvMT16pzvO8smwLrkC9bEjMvanogTZChmjpOuZBF69dITGZB"
            val user2insta = "EAAHO4difc4UBAMrx7ZCRMGS80TA7Ws4M7UAmgg6JI59gBnOrGemsqMby11SCZAwJo29J2XPZAuwewszzKneyS8dHh7HvJwtI90NMB4BZAnAVhWFeeIkYkZCRquj3jdR1XZCtSuakUp1sPKG8SXb4P3DzCMMHdcljkvsZAhXDzPH2lssob6DZBmRcV5V5gF113mUZD"
            val socialMedia2 = mutableSetOf(
                SocialMedia(
                    token = user2insta,
                    socialMediaType = SocialMediaType.INSTAGRAM,
                    user = user,
                    nativeId = 17841452798453962
                ),
                SocialMedia(
                    token = user2token,
                    socialMediaType = SocialMediaType.FACEBOOK_USER,
                    user = user2,
                    nativeId = null
                )
            )
            user2.socialMediaSet = socialMedia2
            userRepository.save(user2)
        }
    }
}
