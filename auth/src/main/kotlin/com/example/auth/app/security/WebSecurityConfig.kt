package com.example.auth.app.security

import com.example.auth.app.CustomAuthenticationManager
import com.example.auth.app.JwtAuthenticationFilter
import com.example.auth.app.jwt.JwtService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy.STATELESS
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport
import java.security.SecureRandom


@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    val userDetailsService: UserDetailsService,
    val jwtService: JwtService,
) : WebSecurityConfigurerAdapter() {

    private val JWT_AUTH_WHITELIST = setOf(
        "/auth/refresh",
        "/auth/facebook/login",
        "/auth/facebook",
        "/register",
        "/register/confirm"
    )

    @Bean
    override fun authenticationManager(): AuthenticationManager {
        return super.authenticationManager()
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(userDetailsService)?.passwordEncoder(encoder())
    }



    @Bean
    fun encoder(): PasswordEncoder = BCryptPasswordEncoder(6, SecureRandom.getInstanceStrong())


    override fun configure(http: HttpSecurity?) {
        http!!.csrf().disable()
            .sessionManagement().sessionCreationPolicy(STATELESS)
            .and()

            .requiresChannel {
                it.anyRequest().requiresSecure()
            }
            .authorizeRequests().antMatchers("auth/refresh")
            .permitAll()
            .and()

            .formLogin()
            .loginPage("/login")
            .permitAll()
            .successForwardUrl("/home")
            .and()


            .authorizeRequests().antMatchers("/auth/facebook/**").fullyAuthenticated()
            .and()


//            .authorizeRequests().anyRequest().permitAll()
//            .and()

            .addFilter(CustomAuthenticationManager(authenticationManagerBean(), jwtService, encoder()))
            .addFilterBefore( JwtAuthenticationFilter(userDetailsService, jwtService, JWT_AUTH_WHITELIST),
                UsernamePasswordAuthenticationFilter::class.java)

    }
}