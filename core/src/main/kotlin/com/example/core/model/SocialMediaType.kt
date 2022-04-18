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
    },

    INSTAGRAM{
        override fun getApiService(): String {
            return "instagramApiService"
        }
    }
    ;

    abstract fun getApiService(): String
}