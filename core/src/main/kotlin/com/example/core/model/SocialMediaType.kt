package com.example.core.model

enum class SocialMediaType {
    FACEBOOK_USER{
        override fun getApiService(): String {
            return "facebookUserApiService"
        }
            },

    FACEBOOK_PAGE{
        override fun getApiService(): String {
            return "facebookPageApiService"
        }
    }
    ;

    abstract fun getApiService(): String
}