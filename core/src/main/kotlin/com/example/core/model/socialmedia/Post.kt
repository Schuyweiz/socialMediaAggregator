package com.example.core.model.socialmedia

import com.example.core.model.SocialMedia
import javax.persistence.*

//todo: full implementation once everything is ready
@Entity
data class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    var id: Long? = null,

    val nativeId: String? = null,

    val textContent: String,

    @ManyToOne
    val socialMedia: SocialMedia,

    val socialMediaType: SocialMediaType? = socialMedia.socialMediaType,


    @Column(name = "likes")
    var likes: Long = 0,

    @OneToMany
    @JoinColumn(name = "post_id")
    val comments: List<Comment> = mutableListOf()

    //todo: comments
) {

    fun incrementLikes() = likes.plus(1)

}