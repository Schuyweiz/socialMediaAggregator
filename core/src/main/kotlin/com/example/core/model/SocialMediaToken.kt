package com.example.core.model

import javax.persistence.*

@Entity(name = "social_media_token")
data class SocialMediaToken(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long = 0,

    var token: String,

    @Column(name = "social_media_type")
    var socialMediaType: SocialMediaType,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User
) {

    fun isFacebook() = socialMediaType == SocialMediaType.FACEBOOK

    override fun toString(): String = """Token is $id, token social media type is ${socialMediaType.name}"""
    override fun hashCode(): Int = id.toInt()
}