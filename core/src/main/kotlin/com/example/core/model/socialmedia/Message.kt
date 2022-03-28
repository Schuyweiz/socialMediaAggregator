package com.example.core.model.socialmedia

import com.example.core.annotation.DefaultCtor
import com.example.core.model.SocialMedia
import java.time.LocalDateTime
import javax.persistence.*

@DefaultCtor
@Entity(name= "message")
data class Message(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    var content: String,

    var nativeId: String,

    @ManyToOne
    var socialMedia: SocialMedia,

    var updateTime: LocalDateTime,


) {

}