package com.example.core.user.model

import javax.persistence.*

@Entity(name = "social_media_token")
data class SocialMediaToken(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = 0,

    val token: String,

    @Column(name = "social_media_type")
    val socialMediaType: SocialMediaType,

    //todo: скорее всего нужно будет чтобы юзер фетчил
    @ManyToOne
    @JoinColumn(name = "app_user")
    val user: User,
) {

}