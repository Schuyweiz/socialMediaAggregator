package com.example.tms.executor

import com.example.tms.service.TmsPostService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class DelayedPostExecutor(
    private val tmsPostService: TmsPostService,
) {

    @Scheduled(cron = "0 * * * * MON-FRI")
    fun processOldData() {
        tmsPostService.publishDelayed()
    }

}