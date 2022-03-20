package com.example.core.model

import java.time.LocalDateTime
import javax.persistence.*


@Entity(name = "verification_tokens")
data class VerificationToken(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    val token: String,

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(nullable = false, referencedColumnName = "id")
    val user: User,

    val expiryDate: LocalDateTime,
)


fun calculateExpiryDate(expiryTimeInMinutes: Int): LocalDateTime =
    LocalDateTime.now().plusMinutes(expiryTimeInMinutes.toLong())