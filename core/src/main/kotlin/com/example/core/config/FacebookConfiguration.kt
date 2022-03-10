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
            addPermission(FacebookPermissions.USER_MANAGED_GROUPS)
            addPermission(FacebookPermissions.USER_POSTS)
            addPermission(FacebookPermissions.PAGES_MANAGE_POSTS)
            addPermission(FacebookPermissions.PAGES_READ_USER_CONTENT)
        }
    }
}