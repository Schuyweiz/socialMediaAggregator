package com.example.api.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class UserTosController {

    @GetMapping("/uterms")
    fun getRussianTos(): String {
        return "uterms.html"
    }
}