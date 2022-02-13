package com.example.auth.util

import javax.servlet.http.Cookie

class CookieUtil {

    companion object {
        @JvmStatic
        fun makeCookie(cookieValue: String, cookieKey: String, maxAge: Int) =
            Cookie(cookieKey, cookieValue).apply {
                this.maxAge = maxAge
                //fixme: figure out the domain thingy
                //this.domain = ".api/auth/"
                this.isHttpOnly = true
                //TODO: set it up for something better wehn done
                this.secure = false
            }
    }


}