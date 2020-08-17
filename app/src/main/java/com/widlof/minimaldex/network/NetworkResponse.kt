package com.widlof.minimaldex.network

sealed class NetworkResponse<out T> {
    data class Success<T>(val responseBody: T): NetworkResponse<T>()
    data class Error(val errorCode: String): NetworkResponse<Nothing>()
}