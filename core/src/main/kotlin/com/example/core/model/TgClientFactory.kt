package com.example.core.model

import it.tdlight.common.TelegramClient
import it.tdlight.jni.TdApi.*
import it.tdlight.tdlight.ClientManager
import java.io.IOError
import java.io.IOException

object TgClientFactory {
    fun createClient(): TelegramClient {
        val telegramClient = ClientManager.create()

//        // Отвечает за подробность логов.
//        telegramClient.execute(SetLogVerbosityLevel(0))
//        // disable TDLib log
//        if (telegramClient.execute(
//                SetLogStream(
//                    LogStreamFile("1_tdlib.log", 1 shl 27, false)
//                )
//            ) is Error
//        ) {
//            throw IOError(IOException("Write access to the current directory is required"))
//        }
        return telegramClient
    }
}