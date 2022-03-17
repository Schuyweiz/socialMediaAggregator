package com.example.core.user.model

enum class SocialMediaType {
    FACEBOOK{
        override fun getApiService(): String {
            return "facebookApiService"
        }
            },
    ;

    abstract fun getApiService(): String
}