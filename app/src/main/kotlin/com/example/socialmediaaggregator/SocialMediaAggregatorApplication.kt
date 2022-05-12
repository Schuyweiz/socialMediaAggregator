package com.example.socialmediaaggregator

import it.tdlight.common.Init
import it.tdlight.common.utils.CantLoadLibrary
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(scanBasePackages = ["com.example.socialmediaaggregator", "com.example.core", "com.example.auth", "com.example.api", "com.example.tms"])
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

}
