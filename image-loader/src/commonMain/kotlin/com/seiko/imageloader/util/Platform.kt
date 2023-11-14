package com.seiko.imageloader.util

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.CoroutineDispatcher
import okio.BufferedSource
import okio.FileSystem

expect class WeakReference<T : Any>(referred: T) {
    fun get(): T?

    fun clear()
}

internal expect suspend fun ByteReadChannel.source(): BufferedSource

internal expect val ioDispatcher: CoroutineDispatcher

internal expect val httpEngine: HttpClientEngine

internal expect val defaultFileSystem: FileSystem?

internal val httpEngineFactory: () -> HttpClient
    get() = { HttpClient(httpEngine) }
