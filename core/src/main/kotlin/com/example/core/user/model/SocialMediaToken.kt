package com.example.core.user.model

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

    @ManyToOne(optional = false, cascade = [CascadeType.PERSIST], fetch = FetchType.LAZY)
    val user: User
) {

}