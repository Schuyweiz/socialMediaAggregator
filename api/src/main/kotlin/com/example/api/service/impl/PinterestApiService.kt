package com.example.api.service.impl

import com.example.api.events.PostRequestedEvent
import com.example.api.mapper.PostMapper
import com.example.api.service.SaveImageExternallyService
import com.example.api.service.SocialMediaPosting
import com.example.core.dto.PostDto
import com.example.core.dto.PublishPostDto
import com.example.core.libs.pinterest.PinterestUserClient
import com.example.core.libs.pinterest.model.CreatePinRequest
import com.example.core.libs.pinterest.model.MediaSource
import com.example.core.model.SocialMedia
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class PinterestApiService(
    private val postMapper: PostMapper,
    private val saveImageExternallyService: SaveImageExternallyService,
    private val eventPublisher: ApplicationEventPublisher,
) : SocialMediaPosting {
    override fun getPosts(socialMedia: SocialMedia): List<PostDto> {
        val client = PinterestUserClient(socialMedia.token)
        val pins = client.getBoards()?.items?.flatMap { client.getPinsFromBoard(it.id)?.items ?: emptyList() }

        return postMapper.map(pins ?: emptyList(), socialMedia.nativeId ?: -1)
            .also { eventPublisher.publishEvent(PostRequestedEvent(socialMedia, it)) }
    }

    override fun publishPost(socialMedia: SocialMedia, postDto: PublishPostDto): PostDto? {
        val client = PinterestUserClient(socialMedia.token)
        val url = postDto.attachment?.let { saveImageExternallyService.saveImage(it) }
            ?: saveImageExternallyService.saveImage(postDto.byteContent!!)
        val response = client.createPin(
            CreatePinRequest(
                null,
                postDto.pinBoardId ?: "",
                postDto.pinSectionId ?: "",
                postDto.content,
                media_source = MediaSource(url = url),
                title = postDto.pinTitle,
            )
        )

        return response?.let { postMapper.map(it, socialMedia.nativeId!!.toLong()) }
    }
}