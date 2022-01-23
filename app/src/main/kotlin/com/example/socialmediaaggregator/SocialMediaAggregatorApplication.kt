package com.example.socialmediaaggregator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SocialMediaAggregatorApplication{


    companion object{
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<SocialMediaAggregatorApplication>(*args)
        }
    }
}
