package com.example.core.model

import com.example.core.annotation.Logger
import com.example.core.annotation.Logger.Companion.log
import it.tdlight.common.ResultHandler
import it.tdlight.common.TelegramClient
import it.tdlight.jni.TdApi
import it.tdlight.jni.TdApi.CheckAuthenticationCode
import it.tdlight.jni.TdApi.CheckAuthenticationPassword
import it.tdlight.tdlight.ClientManager
import org.aspectj.apache.bcel.generic.InstructionConstants.Clinit
import java.util.*
import java.util.concurrent.CountDownLatch

@Logger
class TgClient(
    var currentStateConstructor: Int? = null,
    var error: String? = null,
    var countDownLatch: CountDownLatch,
    var client: TelegramClient,
    val token: String = UUID.randomUUID().toString(),
    var loggedIn: Boolean = false,
) {

    fun init() = client.initialize(
        TgUpdateHandler(),
        { exception -> log.error(exception.message) },
        { exception -> log.error(exception.message) })


    fun authPhoneNumber(number: String) = client.send(TdApi.SetAuthenticationPhoneNumber(number, null), AuthHandler())

    fun authCode(code: String?) = client.send(
        CheckAuthenticationCode(code),
        AuthHandler()
    )

    fun authPassword(password: String?) = client.send(CheckAuthenticationPassword(password), AuthHandler())


    private inner class TgUpdateHandler(
    ) : ResultHandler<TdApi.Update> {
        override fun onResult(`object`: TdApi.Object) {
            when (`object`.constructor) {
                TdApi.UpdateAuthorizationState.CONSTRUCTOR -> handleAuth((`object` as TdApi.UpdateAuthorizationState).authorizationState)
            }
        }

        private fun handleAuth(authState: TdApi.AuthorizationState) = when (authState.constructor) {
            TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR -> {
                val parameters = TdApi.TdlibParameters()
                parameters.databaseDirectory = "tgUsers/$token"
                parameters.useMessageDatabase = true
                parameters.useChatInfoDatabase = true
                parameters.useFileDatabase = true
                parameters.useSecretChats = false
                parameters.apiId = 12419938
                parameters.apiHash = "22033f3a61b16e795125da5d8beb8b92"
                parameters.systemLanguageCode = "ru"
                parameters.deviceModel = "Desktop"
                parameters.applicationVersion = "1.0"
                parameters.enableStorageOptimizer = true

                client.send(
                    TdApi.SetTdlibParameters(parameters), AuthHandler()
                )

            }
            TdApi.AuthorizationStateWaitEncryptionKey.CONSTRUCTOR -> client.send<TdApi.Ok>(
                TdApi.CheckDatabaseEncryptionKey(),
                AuthHandler()
            )
            TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR -> {
                currentStateConstructor = TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR
                countDownLatch.countDown()
            }
            TdApi.AuthorizationStateWaitOtherDeviceConfirmation.CONSTRUCTOR -> {
                val link = (authState as TdApi.AuthorizationStateWaitOtherDeviceConfirmation).link
                println("Please confirm this login link on another device: $link")
            }
            TdApi.AuthorizationStateWaitCode.CONSTRUCTOR -> {
                error = null
                currentStateConstructor = TdApi.AuthorizationStateWaitCode.CONSTRUCTOR
                countDownLatch.countDown()
            }
            TdApi.AuthorizationStateWaitPassword.CONSTRUCTOR -> {
                error = null
                currentStateConstructor = TdApi.AuthorizationStateWaitPassword.CONSTRUCTOR
                countDownLatch.countDown()
            }
            TdApi.AuthorizationStateReady.CONSTRUCTOR -> {
                error = null
                currentStateConstructor = TdApi.AuthorizationStateReady.CONSTRUCTOR
                countDownLatch.countDown()
            }
            TdApi.AuthorizationStateClosed.CONSTRUCTOR -> {
                client = ClientManager.create()
                init()
            }
            else -> System.err.println("Unsupported authorization state: $authState")
        }
    }

    private inner class AuthHandler : ResultHandler<TdApi.Ok> {
        override fun onResult(`object`: it.tdlight.jni.TdApi.Object) {
            if (`object`.constructor == it.tdlight.jni.TdApi.Error.CONSTRUCTOR)
                error = (`object` as it.tdlight.jni.TdApi.Error).message
            countDownLatch.countDown()
        }

    }
}