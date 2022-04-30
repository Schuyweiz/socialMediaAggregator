package com.example.core.model

import com.example.core.dto.SocialMediaDto
import javax.persistence.*

@Entity(name = "social_media")
data class SocialMedia(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long = 0,

    @Column(nullable = true)
    var nativeId: Long?,

    var token: String,

    @Column(name = "social_media_type")
    var socialMediaType: SocialMediaType,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User
) {

    fun isFacebook() = socialMediaType == SocialMediaType.FACEBOOK_USER

    override fun toString(): String = """Token is $id, token social media type is ${socialMediaType.name}"""
    override fun hashCode(): Int = id.toInt()
}

fun SocialMedia.toDto() = SocialMediaDto(
    id = this.id,
    nativeId = this.nativeId,
    token = this.token,
    socialMediaType = this.socialMediaType,

    )