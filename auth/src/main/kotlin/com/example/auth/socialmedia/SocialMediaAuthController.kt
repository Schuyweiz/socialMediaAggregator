package com.example.auth.socialmedia

import com.example.core.utils.Logger
import com.example.core.utils.Logger.Companion.log
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.result.view.RedirectView

@Logger
@RestController
class SocialMediaAuthController() {


    @GetMapping("/auth/facebook/login")
    fun facebookLoginDialogue(
    ): RedirectView {
        return RedirectView(FACEBOOK_LOGIN_DIALOGUE_URL)
    }

    @GetMapping("/")
    fun facebokstuff(model: Model) {
        System.out.println("Controller")
    }

    @GetMapping("auth/facebook")
    fun getFacebookCode(
        @RequestParam(name = "code") code: String,
    ) {
        log.info("""The code is extracted and is $code""")
    }

    companion object {
        const val FACEBOOK_LOGIN_DIALOGUE_URL = "https://www.facebook.com/v13.0/dialog/oauth?" +
                "client_id=1005503330221024" +
                "&redirect_uri=https://localhost:8443/auth/facebook" +
                "&response_type=code" +
                "&state={\"{st=state123abc,ds=123456789}\"}"
    }
}