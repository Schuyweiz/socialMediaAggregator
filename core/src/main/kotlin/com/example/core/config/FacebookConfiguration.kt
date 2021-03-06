package com.example.core.config

import com.restfb.DefaultFacebookClient
import com.restfb.Version
import com.restfb.scope.FacebookPermissions
import com.restfb.scope.ScopeBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FacebookConfiguration {

    @Bean
    fun facebookClient() = DefaultFacebookClient(Version.LATEST)

    companion object {
        val scope: ScopeBuilder = with(ScopeBuilder()) {
            addPermission(FacebookPermissions.USER_POSTS)
            addPermission(FacebookPermissions.PAGES_MANAGE_POSTS)
            addPermission(FacebookPermissions.PAGES_READ_ENGAGEMENT)
            addPermission(FacebookPermissions.PAGES_SHOW_LIST)
            addPermission(FacebookPermissions.PAGES_MESSAGING)
            addPermission(FacebookPermissions.PAGES_READ_USER_CONTENT)
            addPermission(FacebookPermissions.PAGES_MANAGE_ENGAGEMENT)
            addPermission(FacebookPermissions.INSTAGRAM_BASIC)
            addPermission(FacebookPermissions.INSTAGRAM_CONTENT_PUBLISH)
            addPermission(FacebookPermissions.INSTAGRAM_MANAGE_COMMENTS)
            addPermission(FacebookPermissions.INSTAGRAM_MANAGE_MESSAGES)
        }
    }
}