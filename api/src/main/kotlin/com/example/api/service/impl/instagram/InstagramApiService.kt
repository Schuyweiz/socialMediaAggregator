package com.example.api.service.impl.instagram

import com.example.api.mapper.PostMapper
import com.example.api.service.SocialMediaPosting
import com.example.core.annotation.Logger
import com.example.core.dto.PostDto
import com.example.core.dto.PostResponseDto
import com.example.core.dto.PublishPostDto
import com.example.core.model.SocialMedia
import com.example.core.service.FacebookApi
import com.restfb.BinaryAttachment
import com.restfb.FacebookClient
import com.restfb.Parameter
import com.restfb.types.instagram.IgMedia
import org.springframework.stereotype.Service

@Logger
@Service
class InstagramApiService(
    private val postMapper: PostMapper,
) : SocialMediaPosting, FacebookApi {

    override fun getPosts(socialMedia: SocialMedia): List<PostDto> {
        val client = getFacebookClient(socialMedia.token)
        val nativeId = socialMedia.nativeId
        val mediaFields =
            "ig_id,children{permalink,media_type,media_url,timestamp},thumbnail_url,shortcode,timestamp,media_type,media_url,is_comment_enabled,like_count,comments{like_count,media,replies,timestamp,user,username,text},permalink,caption"

        val listMedia = client.fetchConnection(
            """$nativeId/media""", IgMedia::class.java, Parameter.with("fields", mediaFields)
        )

        return listMedia.data.map { postMapper.map(it) }
    }

    override fun publishPost(socialMedia: SocialMedia, postDto: PublishPostDto): PostDto {
        val client = getFacebookClient(socialMedia.token)
        val nativeId = socialMedia.nativeId

        val response = postDto.attachment?.let { publishMediaPost(client, nativeId!!, postDto) }
            ?: publishTextPost(client, nativeId!!, postDto)


        return client.fetchObject(response.postId, PostDto::class.java).apply {
            this.pageId = socialMedia.nativeId ?: -1
            this.socialMediaType = socialMedia.socialMediaType
        }
            ?: throw Exception("Something went wrong, post id is ${response.postId}")
    }

    private fun publishMediaPost(client: FacebookClient, nativeId: Long, postDto: PublishPostDto) = client.publish(
        """$nativeId/media""",
        PostResponseDto::class.java,
        BinaryAttachment.with(postDto.attachment!!.name, postDto.attachment!!.bytes, postDto.attachment!!.contentType),
        Parameter.with("message", postDto.content)
    )

    private fun publishTextPost(client: FacebookClient, nativeId: Long, postDto: PublishPostDto) = client.publish(
        """$nativeId/media""",
        PostResponseDto::class.java,
        Parameter.with("message", postDto.content)
    )
}