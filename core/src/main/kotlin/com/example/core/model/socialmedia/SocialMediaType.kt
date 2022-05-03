package com.example.core.model.socialmedia

enum class SocialMediaType {
    FACEBOOK_USER {
        override fun getApiService(): String = "facebookUserApiService"
    },

    FACEBOOK_PAGE {
        override fun getApiService(): String = "facebookPageApiService"
    },

    INSTAGRAM {
        override fun getApiService(): String = "instagramApiService"
    },

    VK_USER {
        override fun getApiService(): String = "vkApiUserService"
    },

    VK_GROUP {
        override fun getApiService(): String = "vkApiGroupService"
    },

    TG {
        override fun getApiService(): String {
            return "tgApiService"
        }
    },

    PINTEREST {
        override fun getApiService(): String = "pinterestApiService"
    }
    ;

    abstract fun getApiService(): String
}