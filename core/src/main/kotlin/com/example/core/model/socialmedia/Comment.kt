package com.example.core.model.socialmedia

import com.example.core.annotation.DefaultCtor
import java.time.Instant
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.ManyToOne

@Entity
data class Comment(

    @EmbeddedId
    var commentId: CommentKey,

    var message: String? = null,

    var fromId: String? = null,

    var createdTime: Instant? = null,

    @ManyToOne
    var post: Post? = null
    ) {

    @Embeddable
    @DefaultCtor
    data class CommentKey(
        @Column(name = "native_id", updatable = false)
        var nativeId: String? =  null,

        @Column(name = "social_media_type", updatable = false)
        var socialMediaType: SocialMediaType,
    ) : java.io.Serializable
}