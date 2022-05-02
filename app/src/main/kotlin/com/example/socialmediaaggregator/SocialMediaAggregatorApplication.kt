package com.example.socialmediaaggregator

import com.example.core.model.SocialMedia
import com.example.core.model.socialmedia.SocialMediaType
import com.example.core.model.User
import com.example.core.model.socialmedia.Post
import com.example.core.repository.PostRepository
import com.example.core.repository.SocialMediaRepository
import com.example.core.repository.UserRepository
import it.tdlight.common.Init
import it.tdlight.common.utils.CantLoadLibrary
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
        init {
            // Скачка библиотеки телеграма
            try {
                Init.start()
            } catch (exception: CantLoadLibrary) {
                exception.printStackTrace()
            }
        }

        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<SocialMediaAggregatorApplication>(*args)
        }
    }

    @Bean
    fun init(
        userRepository: UserRepository,
        socialMediaTokenRepository: SocialMediaRepository,
        encoder: PasswordEncoder,
        postRepository: PostRepository,
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
            val user2 = User(
                firstName = "kirill", lastName = "pike",
                userPassword = encoder.encode("1234"), email = "pikek@mail.ru", enabled = true
            )
            val socialMedia =
                mutableSetOf(
                    SocialMedia(
                        token = pageToken,
                        socialMediaType = SocialMediaType.FACEBOOK_PAGE,
                        user = user2,
                        nativeId = 110351558278776
                    ),
                    SocialMedia(
                        token = userToken,
                        socialMediaType = SocialMediaType.FACEBOOK_USER,
                        user = user2,
                        nativeId = null
                    )
                )
            val user2token =
                "EAAHO4difc4UBAHj3SDtCBm3y3cxrSz19o9DknpOu9UcQKTwVFgzqFuJaaZCt2CX0Xpkd7dwgBZCe9Jihca8HfHRWD8jeaj3mjZAt2wq8602YZCHAec4H013lmcMlB80hjMT5m4ngvMT16pzvO8smwLrkC9bEjMvanogTZChmjpOuZBF69dITGZB"
            val user2insta =
                "EAAHO4difc4UBALVo4h4aj1dvaoJlV3MggP16f4J6oZCTlQrxuxUFGfaursrzP0AZCC1EVCqvjqWlvwLWYhqitKn9IqN6JZB07Af5IjU86rJqJl2bUSq5n5QmKSEnNcZCVrbg5XD2EIKNbYsLp281mhyHc0gI1q7b2sjQcwSPMpzS7q9aRaymv9hPhLyHu5gZD"
            val socialMedia2 = mutableSetOf(
                SocialMedia(
                    token = user2insta,
                    socialMediaType = SocialMediaType.INSTAGRAM,
                    user = user2,
                    nativeId = 17841452798453962
                ),
                SocialMedia(
                    token = user2token,
                    socialMediaType = SocialMediaType.FACEBOOK_USER,
                    user = user2,
                    nativeId = null
                ),
                SocialMedia(
                    token = pageToken,
                    socialMediaType = SocialMediaType.FACEBOOK_PAGE,
                    user = user2,
                    nativeId = 110351558278776
                ),
                SocialMedia(
                    token = userToken,
                    socialMediaType = SocialMediaType.FACEBOOK_USER,
                    user = user2,
                    nativeId = null
                ),
                SocialMedia(
                    token = "EAAHO4difc4UBAH0VSSY7c7Gs5yEkjtFMHs8JzUaFopl4dZBaZB6r7PnZAmdElZBz8JesVkbfjNgFr710CZBKZC2tCG7fDFrMtCZAFhqcg4GlJxAAycgUY2w6zEq6ohjwbS1aSEJusvIeFwzrMqa4IKYtrnxOZCWKDO18UJjGvorXwn3UImBCGC5ioZC2sHJlAL44ZD",
                    socialMediaType = SocialMediaType.FACEBOOK_PAGE,
                    user = user2,
                    nativeId = 104426122254580
                )
            )
            user2.socialMediaSet = socialMedia2
            userRepository.save(user2)
        }
    }
}
