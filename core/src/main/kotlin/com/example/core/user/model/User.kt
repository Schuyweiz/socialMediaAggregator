package com.example.core.user.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*
import org.springframework.security.core.userdetails.User as SecUser

@Entity(name = "app_user")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0,

    @Column(name = "first_name")
    val firstName: String,

    @Column(name = "last_name")
    val lastName: String,

    @Column(name = "password")
    var userPassword: String,

    @Column(name="email", unique = true)
    val email: String,

    var enabled: Boolean = false,

    @kotlin.jvm.Transient
    @ElementCollection
    var authorities: MutableList<SimpleGrantedAuthority> = mutableListOf(SimpleGrantedAuthority("ROLE_USER")),

    @OneToMany
    @JoinColumn(name = "id", nullable = true, updatable = true)
    var socialMediaTokens: MutableSet<SocialMediaToken> = mutableSetOf()

): UserDetails{
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = authorities

    override fun getPassword(): String = userPassword

    override fun getUsername(): String = email

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = enabled
}

