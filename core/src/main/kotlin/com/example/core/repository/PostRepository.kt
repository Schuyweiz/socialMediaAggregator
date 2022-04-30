package com.example.core.repository

import com.example.core.model.SocialMedia
import com.example.core.model.SocialMediaType
import com.example.core.model.socialmedia.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface PostRepository : JpaRepository<Post, Long> {

    fun findAllByNativeIdAndSocialMedia(nativeId: String, socialMedia: SocialMedia): Set<Post>

    @Query(
        "select p from Post p " +
                "join social_media sm on p.socialMedia =sm " +
                "where sm.socialMediaType in (:types) and p.nativeId = :nativeId"
    )
    fun selectAllByNativeIdAndSocialMediaType(
        @Param("nativeId") nativeId: String,
        @Param("types") socialMediaTypes: Set<SocialMediaType>
    ): Set<Post>

    @Modifying
    @Query(
        value =
        "update Post " +
                "set likes = likes + 1 " +
                "where id = ?1"
    )
    fun incrementLikes(@Param("id") postId: Long)
}