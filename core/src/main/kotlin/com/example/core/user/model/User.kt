package com.example.core.user.model

import lombok.Setter
import javax.persistence.*

@Setter
@Entity(name = "app_user")
@SuppressWarnings
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") var id: Long? = null,
    @Column(name = "first_name") val firstName: String,
    @Column(name = "second_name")
    val secondName: String,
    @Column(name = "username")
    val username: String,
    //TODO: remove when you manage to call it properly in the db
    @Column(name = "pswdhash")
    val passwordHash: String,
    val mail: String,
    var enabled: Boolean = false,
)