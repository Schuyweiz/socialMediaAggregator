package com.example.auth.app.security

import com.example.auth.app.CustomAuthenticationManager
import com.example.auth.app.JwtAuthenticationFilter
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

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
            .authorizeRequests().antMatchers("/login").permitAll()
            .and()
            .authorizeRequests().antMatchers(GET, "api/user/**").hasAuthority("ROLE_USER")
            .and()
            .authorizeRequests().anyRequest().authenticated()
            .and()
            .addFilter(CustomAuthenticationManager(authenticationManagerBean(), jwtService))

        http.addFilterBefore(
            JwtAuthenticationFilter(userDetailsService, jwtService),
            UsernamePasswordAuthenticationFilter::class.java
        )

    }
}