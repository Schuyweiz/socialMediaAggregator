package com.example.core.model

import it.tdlight.common.ExceptionHandler
import it.tdlight.common.ResultHandler
import it.tdlight.common.internal.InternalClient
import it.tdlight.jni.TdApi

fun InternalClient.initialize(
    updateHandler: ResultHandler<TdApi.Update>,
    updateExceptionHandler: ExceptionHandler,
    defaultExceptionHandler: ExceptionHandler,
    clientId: Int
): Unit {
    javaClass.getDeclaredField("updateHandler").let { field ->
        field.isAccessible = true
        field.set(this, updateHandler)
    }

    javaClass.getDeclaredField("updateExceptionHandler").let { field ->
        field.isAccessible = true
        field.set(this, updateExceptionHandler)
    }

    javaClass.getDeclaredField("defaultExceptionHandler").let { field ->
        field.isAccessible = true
        field.set(this, defaultExceptionHandler)
    }


    javaClass.getDeclaredField("clientId").let { field ->
        field.isAccessible = true
        field.set(this, clientId)
    }

}