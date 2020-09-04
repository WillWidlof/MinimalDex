package com.widlof.minimaldex.network

import okhttp3.OkHttpClient

class NetworkClientBuilder {

    fun buildDefaultNetworkClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }
}