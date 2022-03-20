package com.example.auth.socialmedia.events

import com.example.auth.socialmedia.FacebookAuthService
import com.example.core.model.FacebookPageToken
import com.example.core.model.SocialMediaToken
import com.example.core.model.SocialMediaType
import com.example.core.user.dao.FacebookPageTokenRepository
import com.example.core.user.repository.SocialMediaTokenRepository
import com.example.core.utils.Logger
import com.example.core.utils.Logger.Companion.log
import org.springframework.context.ApplicationListener
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Logger
@Component
class SocialMediaAuthenticationEventListener(
    private val facebookAuthService: FacebookAuthService,
    private val socialMediaTokenRepository: SocialMediaTokenRepository,
    private val facebookPageTokenRepository: FacebookPageTokenRepository
) : ApplicationListener<OnSocialMediaAuthenticationEvent> {


    @Transactional
    override fun onApplicationEvent(event: OnSocialMediaAuthenticationEvent) {
        val token = event.token
        val user = event.user
        val extendedToken = facebookAuthService.getExtendedAccessToken(token.accessToken)

        log.info("""long lived access token is ${extendedToken.accessToken} expires in ${extendedToken.expires}""")

        socialMediaTokenRepository.save(
            SocialMediaToken(
            token = extendedToken.accessToken,
            socialMediaType = SocialMediaType.FACEBOOK,
            user = user)
        )
    }


    @EventListener
    fun onFacebookPageAuthentication(event: OnFacebookPageAuthenticationEvent){
        val extendedToken = facebookAuthService.getExtendedAccessToken(event.pageToken)
        log.info("""long lived token for facebook page is ${extendedToken.accessToken} expires on ${extendedToken.expires}""")

        facebookPageTokenRepository.save(FacebookPageToken(token= extendedToken.accessToken, socialMediaToken = event.userToken))
    }
}