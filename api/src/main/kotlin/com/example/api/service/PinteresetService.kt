package com.example.api.service

import com.example.api.service.impl.PinterestApiService
import com.example.core.libs.pinterest.PinterestUserClient
import com.example.core.libs.pinterest.model.*
import com.example.core.service.impl.UserQueryService
import org.springframework.stereotype.Service

@Service
class PinteresetService(
    private val pinterestApiService: PinterestApiService,
    private val userQueryService: UserQueryService,
) {

    fun getBoards(userId: Long, accountId: Long): List<Item> {
        val user = userQueryService.findByIdOrThrow(userId)
        val pinterestAccount =
            user.socialMediaSet.singleOrNull { it.nativeId == accountId } ?: throw Exception("Something went wrong")
        val client = PinterestUserClient(pinterestAccount.token)

        return client.getBoards()?.items ?: emptyList()
    }

    fun getBoardSections(userId: Long, accountId: Long, boardId: String): List<SectionDto> {
        val user = userQueryService.findByIdOrThrow(userId)
        val pinterestAccount =
            user.socialMediaSet.singleOrNull { it.nativeId == accountId } ?: throw Exception("Something went wrong")
        val client = PinterestUserClient(pinterestAccount.token)

        return client.getBoardSections(boardId)?.items ?: emptyList()
    }

    fun createBoard(userId: Long, accountId: Long, request: CreatePinterestBoardRequest): CreateBoardResponse? {
        val user = userQueryService.findByIdOrThrow(userId)
        val pinterestAccount =
            user.socialMediaSet.singleOrNull { it.nativeId == accountId } ?: throw Exception("Something went wrong")
        val client = PinterestUserClient(pinterestAccount.token)

        return client.createBoard(request)
    }

    fun createBoardSection(userId: Long, accountId: Long, boardId: String, request: CreateSectionRequest): SectionDto? {
        val user = userQueryService.findByIdOrThrow(userId)
        val pinterestAccount =
            user.socialMediaSet.singleOrNull { it.nativeId == accountId } ?: throw Exception("Something went wrong")
        val client = PinterestUserClient(pinterestAccount.token)

        return client.createBoardSection(boardId, request)
    }
}