package com.widlof.minimaldex.network

import okhttp3.Request

class NetworkRequestBuilder {
    fun buildJsonRequest(url: String, contentType: String = CONTENT_TYPE_JSON ): Request {
        return Request.Builder()
            .url(url)
            .addHeader(CONTENT_TYPE_KEY, contentType)
            .build()
    }

    fun buildBitmapRequest(url: String, contentType: String = CONTENT_TYPE_JSON ): Request {
        return Request.Builder()
            .url(url)
            .build()
    }

    companion object {
        private const val CONTENT_TYPE_KEY = "Content-Type"
        private const val CONTENT_TYPE_JSON = "application/json"
    }
}