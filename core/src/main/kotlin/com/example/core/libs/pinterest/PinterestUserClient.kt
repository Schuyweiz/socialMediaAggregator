package com.example.core.libs.pinterest

import com.example.core.libs.pinterest.model.*
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders

class PinterestUserClient(
    private val token: String,
    private val adAccountId: Long? = null,
) {
    private val userTemplate = RestTemplateBuilder().rootUri("https://api.pinterest.com/v5/")
        .defaultHeader(HttpHeaders.AUTHORIZATION, """Bearer $token""").build()

    private val sectionPath = "/boards/{boardId}/sections"
    private val boardsPinsPath = "/boards/{boardId}/pins"
    private val boardPath = "/boards/{boardId}"

    fun getUsers() =
        userTemplate.getForEntity("""user_account?ad_account=$adAccountId""", PinterestUserAccountDto::class.java).body

    fun getBoards(pageSize: Int = 25, bookmark: String? = null, privacy: String = "PUBLIC") =
        userTemplate.getForEntity(getBoardsParams(pageSize, bookmark, privacy), PinterestBoardsDto::class.java).body

    fun createBoard(request: CreatePinterestBoardRequest): CreateBoardResponse? {
        val entity = HttpEntity(request)
        return userTemplate.postForEntity("/boards", entity, CreateBoardResponse::class.java).body
    }

    fun getBoardSections(boardId: String, bookmark: String? = null, pageSize: Int = 25) = userTemplate.getForEntity(
        paramBuilder(sectionPath, hashMapOf("bookmark" to bookmark, "page_size" to pageSize)),
        PinterestSectionsDto::class.java,
        boardId
    ).body

    fun createBoardSection(boardId: String, request: CreateSectionRequest) =
        userTemplate.postForEntity(sectionPath, HttpEntity(request), SectionDto::class.java, boardId).body

    fun getPinsFromBoard(boardId: String, bookmark: String? = null, pageSize: Int = 25) =
        userTemplate.getForEntity(
            paramBuilder(
                boardsPinsPath,
                hashMapOf("bookmark" to bookmark, "page_size" to pageSize)
            ), PinterestBoardPinsDto::class.java, boardId
        ).body

    fun createPin(createPinRequest: CreatePinRequest) =
        userTemplate.postForEntity("/pins", HttpEntity(createPinRequest), CreatePinResponseDto::class.java).body

    fun getBoard(boardId: String) = userTemplate.getForEntity(boardPath, PinterestBoardsDto::class.java, boardId).body

    private fun getBoardsParams(pageSize: Int, bookmark: String?, privacy: String) =
        paramBuilder("/boards", hashMapOf("page_size" to pageSize, "bookmark" to bookmark, "privacy" to privacy))


    private fun paramBuilder(prefix: String, params: HashMap<String, Any?>) =
        params.entries.filterNot { entry -> entry.value == null }.joinToString(
            separator = "&",
            prefix = "$prefix?",
            transform = { entry -> """${entry.key}=${entry.value}""" })
}