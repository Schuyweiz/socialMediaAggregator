package com.example.tms.executor

import com.example.core.annotation.Logger
import com.example.tms.service.TmsPostService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Logger
@Component
class CheckOldDataExecutorConfig(
    private val postService: TmsPostService,
) {

    @Scheduled(cron = "0 * * * * MON-FRI")
    fun processOldData() {
        postService.processExpiredPosts()
    }
}