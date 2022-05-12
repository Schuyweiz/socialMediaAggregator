package com.example.core.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Entity(name = "app_user")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long = 0,

    @Column(name = "first_name")
    val firstName: String?,

    @Column(name = "last_name")
    val lastName: String?,

    @Column(name = "password")
    val userPassword: String?,

    @Column(name = "email", unique = true)
    val email: String?,

    var enabled: Boolean = true,

    @kotlin.jvm.Transient
    @ElementCollection
    var authorities: MutableList<SimpleGrantedAuthority> = mutableListOf(SimpleGrantedAuthority("ROLE_USER")),

    @OneToMany(mappedBy = "user", cascade = [CascadeType.MERGE, CascadeType.PERSIST])
    var socialMediaSet: MutableSet<SocialMedia> = mutableSetOf(),

    @OneToOne(cascade = [CascadeType.ALL])
    var webhooks: Webhooks? = null,

    ) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        mutableSetOf(SimpleGrantedAuthority("ROLE_USER"))

    override fun getPassword(): String = userPassword.orEmpty()

    override fun getUsername(): String = email.orEmpty()

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = enabled

    override fun hashCode(): Int {
        return id.toInt()
    }
}

