package com.example.core.model

import javax.persistence.*

@Entity(name = "webhooks")
data class Webhooks(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    var postWebhook: String? = null,
    var commentWebhook: String? = null,
) {


}