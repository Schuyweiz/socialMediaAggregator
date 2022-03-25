package com.example.core.model.socialmedia

import com.example.core.model.SocialMedia
import com.example.core.model.SocialMediaType
import javax.persistence.*

//todo: full implementation once everything is ready
@Entity
data class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    val textContent: String,

    @ManyToOne
    val socialMedia: SocialMedia

    //todo: comments
) {


}