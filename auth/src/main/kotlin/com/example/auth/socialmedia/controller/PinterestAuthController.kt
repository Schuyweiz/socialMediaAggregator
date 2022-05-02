package com.example.auth.socialmedia.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class PinterestAuthController(

) {


    @GetMapping("auth/pinterest")
    fun pinterestRecieveCode(
        @RequestParam(name = "code") code: String,
    ): String {
        return code
    }
}