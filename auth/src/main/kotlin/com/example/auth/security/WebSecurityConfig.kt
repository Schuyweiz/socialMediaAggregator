package com.example.auth.security

import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.JWTVerifier
import com.example.auth.CustomAuthenticationManager
import com.example.auth.JwtAuthenticationFilter
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class WebSecurityConfig(
    val userDetailsService: UserDetailsService,
    val encoder: PasswordEncoder,
    val algorithm: Algorithm,
    val jwtVerifier: JWTVerifier,
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
            .authorizeRequests().antMatchers("/login").permitAll()
            .and()
            .authorizeRequests().antMatchers(GET, "api/user/**").hasAuthority("ROLE_USER")
            .and()
            .authorizeRequests().anyRequest().authenticated()
            .and()
            .addFilter(CustomAuthenticationManager(authenticationManagerBean(), algorithm))

        http.addFilterBefore(
            JwtAuthenticationFilter(userDetailsService, jwtVerifier),
            UsernamePasswordAuthenticationFilter::class.java
        )

    }
}