package com.example.auth.app.security

import com.example.auth.app.CustomAuthenticationManager
import com.example.auth.app.jwt.JwtService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod.GET
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder


@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    val userDetailsService: UserDetailsService,
    val encoder: PasswordEncoder,
    val jwtService: JwtService,
) : WebSecurityConfigurerAdapter() {


    @Bean
    override fun authenticationManager(): AuthenticationManager {
        return super.authenticationManager()
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(userDetailsService)?.passwordEncoder(encoder)
    }

    override fun configure(http: HttpSecurity?) {
        http!!.csrf().disable()
            .sessionManagement().sessionCreationPolicy(STATELESS)
            .and()

            .requiresChannel {
                it.anyRequest().requiresSecure()
            }


            .authorizeRequests().antMatchers("/login").permitAll()
            .and()


            .authorizeRequests().antMatchers(GET, "api/user/**").hasAuthority("ROLE_USER")
            .and()


            .authorizeRequests().anyRequest().permitAll()
            .and()

            .addFilter(CustomAuthenticationManager(authenticationManagerBean(), jwtService))

//        http.addFilterBefore(
//            JwtAuthenticationFilter(userDetailsService, jwtService),
//            UsernamePasswordAuthenticationFilter::class.java
//        )

    }


    companion object {
        const val FACEBOOK_LOGIN_DIALOGUE_URL = "https://www.facebook.com/v13.0/dialog/oauth?" +
                "client_id=1005503330221024" +
                "&redirect_uri=https://localhost:8443/auth/facebook" +
                "&response_type=code" +
                "&state={\"{st=state123abc,ds=123456789}\"}"
    }
}