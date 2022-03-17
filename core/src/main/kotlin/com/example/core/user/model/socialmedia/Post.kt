package com.example.core.user.model.socialmedia

import javax.persistence.*

//todo: full implementation once everything is ready
@Entity
data class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    val textContent: String,

    val socialMediaId: Long,

    //todo: comments
) {


}