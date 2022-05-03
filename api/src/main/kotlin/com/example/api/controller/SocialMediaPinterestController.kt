package com.example.api.controller

import com.example.api.service.PinteresetService
import com.example.core.annotation.JwtSecureEndpoint
import com.example.core.annotation.RestControllerJwt
import com.example.core.libs.pinterest.model.CreatePinterestBoardRequest
import com.example.core.libs.pinterest.model.CreateSectionRequest
import com.example.core.model.User
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@RestControllerJwt
class SocialMediaPinterestController(
    private val pinteresetService: PinteresetService,
) {

    @JwtSecureEndpoint
    @GetMapping("api/pinterest/{accountId}/boards")
    fun getBoards(
        @AuthenticationPrincipal user: User,
        @PathVariable(name = "accountId") accountId: Long,
    ) = pinteresetService.getBoards(user.id, accountId)

    @JwtSecureEndpoint
    @GetMapping("api/pinterest/{accountId}/boards/{boardId}")
    fun getBoardSections(
        @AuthenticationPrincipal user: User,
        @PathVariable(name = "accountId") accountId: Long,
        @PathVariable(name = "boardId") boardId: String
    ) = pinteresetService.getBoardSections(user.id, accountId, boardId)

    @JwtSecureEndpoint
    @PostMapping("api/pinterest/{accountId}/boards/{boardId}/section")
    fun createBoardSection(
        @AuthenticationPrincipal user: User,
        @PathVariable(name = "accountId") accountId: Long,
        @PathVariable(name = "boardId") boardId: String,
        @RequestBody request: CreateSectionRequest,
    ) = pinteresetService.createBoardSection(user.id, accountId, boardId, request)

    @JwtSecureEndpoint
    @PostMapping("api/pinterest/{accountId}/boards")
    fun createBoard(
        @AuthenticationPrincipal user: User,
        @PathVariable(name = "accountId") accountId: Long,
        @RequestBody createBoardRequest: CreatePinterestBoardRequest,
    ) = pinteresetService.createBoard(user.id, accountId, createBoardRequest)
}