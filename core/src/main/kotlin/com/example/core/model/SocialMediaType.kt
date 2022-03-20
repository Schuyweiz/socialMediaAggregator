package com.example.core.model

enum class SocialMediaType {
    FACEBOOK{
        override fun getApiService(): String {
            return "facebookApiService"
        }
            },
    ;

    abstract fun getApiService(): String
}