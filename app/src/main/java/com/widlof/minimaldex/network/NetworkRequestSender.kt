package com.widlof.minimaldex.network

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.BufferedInputStream
import java.io.InputStream


class NetworkRequestSender {
    suspend fun makeJsonRequest(url: String): NetworkResponse<String?>
    = withContext(Dispatchers.IO) {
        val client: OkHttpClient = OkHttpClient.Builder()
            .build()
        val request: Request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .build()
        val response = client.newCall(request).execute()
        val json = response.body?.string()
        Log.d("JSON RESPONSE:", json)
        if (response.isSuccessful) {
            return@withContext (NetworkResponse.Success(json))
        } else {
            val errorCode = response.code.toString()
            return@withContext (NetworkResponse.Error("HTTP Error: $errorCode"))
        }
    }

    suspend fun makeImageRequest(url: String): NetworkResponse<Bitmap?>
            = withContext(Dispatchers.IO) {
        val client: OkHttpClient = OkHttpClient.Builder()
            .build()
        val request: Request = Request.Builder()
            .url(url)
            .build()
        val response = client.newCall(request).execute()
        val responseBody = response.body
        val inputStream: InputStream? = responseBody?.byteStream()
        val bufferedInputStream = BufferedInputStream(inputStream)
        val bitmap = BitmapFactory.decodeStream(bufferedInputStream)
        Log.i("bitmap", "bitmap value = $bitmap")
        if (response.isSuccessful) {
            return@withContext (NetworkResponse.Success(bitmap))
        } else {
            val errorCode = response.code.toString()
            return@withContext (NetworkResponse.Error("HTTP Error: $errorCode"))
        }
    }
}