package com.example.core.model

import com.example.core.utils.DefaultCtor
import javax.persistence.*

@DefaultCtor
@Entity
data class FacebookPageToken(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    var token: String,

    @ManyToOne
    var socialMediaToken: SocialMediaToken
) {

    override fun hashCode(): Int = id!!.toInt()
    override fun toString(): String = """Facebook page token id $id"""
}