package com.widlof.minimaldex.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

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
}