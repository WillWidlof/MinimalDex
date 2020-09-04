package com.widlof.minimaldex.network

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException
import java.io.BufferedInputStream
import java.io.InputStream
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class NetworkRequestSender(private val dispatcher: CoroutineDispatcher,
                           private val networkRequestBuilder: NetworkRequestBuilder,
                           private val networkClientBuilder: NetworkClientBuilder) {

    private var scope: CoroutineScope = CoroutineScope(dispatcher)

    suspend fun makeJsonRequest(url: String): String? =
        suspendCoroutine { continuation ->
            scope.launch {
                val client = networkClientBuilder.buildDefaultNetworkClient()
                runCatching {
                    client.newCall(networkRequestBuilder.buildJsonRequest(url)).execute()
                }.onSuccess {
                    with(it) {
                        val json = body?.string()
                        if (isSuccessful) {
                            continuation.resumeWith(Result.success(json))
                        } else {
                            val errorCode = code.toString()
                            continuation.resumeWith(Result.failure(Exception("HTTP Error: $errorCode")))
                        }
                    }
                }.onFailure {
                    continuation.resumeWithException(it)
                }
            }
        }

    suspend fun makeImageRequest(url: String): Bitmap? =
        suspendCoroutine { continuation ->
            scope.launch {
            val client: OkHttpClient = networkClientBuilder.buildDefaultNetworkClient()
            val request: Request = networkRequestBuilder.buildBitmapRequest(url)
                runCatching {
                    client.newCall(request).execute()
                }.onSuccess {
                    val responseBody = it.body
                    if (responseBody == null) {
                        continuation.resumeWithException(IOException("Bitmamp is null"))
                    } else {
                        val inputStream: InputStream? = responseBody?.byteStream()
                        val bufferedInputStream = BufferedInputStream(inputStream)
                        val bitmap = BitmapFactory.decodeStream(bufferedInputStream)
                        continuation.resumeWith(Result.success(bitmap))
                    }
                }.onFailure {
                    continuation.resumeWithException(it)
                }
            }
        }
}