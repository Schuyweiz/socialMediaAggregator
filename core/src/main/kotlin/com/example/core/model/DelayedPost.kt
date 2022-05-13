package com.example.core.model

import com.example.core.model.socialmedia.Post
import org.springframework.web.multipart.MultipartFile
import java.time.Instant
import javax.persistence.*

@Entity
data class DelayedPost(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "delayed_post_gen")
    @SequenceGenerator(name = "delayed_post_gen", sequenceName = "delayed_post_seq", allocationSize = 10)
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    val content: String,
    @Lob
    val attachment: ByteArray?,
    val pinBoardId: String?,
    val pinSectionId: String?,
    val pinTitle: String?,
    val timeToPost: Instant?,
    val socialMediaId: Long?,
    val userId: Long?,

    val timePublished: Instant,
) {


}