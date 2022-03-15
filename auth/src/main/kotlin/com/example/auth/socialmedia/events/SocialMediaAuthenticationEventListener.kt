package com.example.auth.socialmedia.events

import com.example.auth.socialmedia.FacebookAuthService
import com.example.core.user.model.SocialMediaToken
import com.example.core.user.model.SocialMediaType
import com.example.core.user.repository.SocialMediaTokenRepository
import com.example.core.user.repository.UserRepository
import com.example.core.utils.Logger
import com.example.core.utils.Logger.Companion.log
import com.restfb.DefaultFacebookClient
import com.restfb.FacebookClient.AccessToken
import org.springframework.context.ApplicationListener
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Logger
@Component
class SocialMediaAuthenticationEventListener(
    private val facebookAuthService: FacebookAuthService,
    private val socialMediaTokenRepository: SocialMediaTokenRepository,
) : ApplicationListener<OnSocialMediaAuthenticationEvent> {


    @Transactional
    override fun onApplicationEvent(event: OnSocialMediaAuthenticationEvent) {
        val token = event.source as AccessToken
        val extendedToken = facebookAuthService.getExtendedAccessToken(token.accessToken)

        log.info("""long lived access token is ${extendedToken.accessToken} expires in ${extendedToken.expires}""")

        socialMediaTokenRepository.save(SocialMediaToken(token = extendedToken.accessToken,
            socialMediaType = SocialMediaType.FACEBOOK))
    }
}