package com.widlof.minimaldex.nationaldex.data

import android.util.Log
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.widlof.minimaldex.nationaldex.data.model.NationalDexResponse
import com.widlof.minimaldex.network.NetworkRequestSender
import com.widlof.minimaldex.network.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DexRemoteDataSource(private val networkRequestSender: NetworkRequestSender): DexDataSource {
    override suspend fun getNationalDex(): NetworkResponse<NationalDexResponse?> = withContext(Dispatchers.IO) {

        val response = networkRequestSender.makeJsonRequest(NATION_DEX_URL)
        return@withContext if (response is NetworkResponse.Success) {
            if (response.responseBody != null) {
                response.responseBody.let {
                    Log.d("JSON RESPONSE:", response.responseBody)
                    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                    val adapter: JsonAdapter<NationalDexResponse>
                            = moshi.adapter(NationalDexResponse::class.java)
                    NetworkResponse.Success(adapter.fromJson(response.responseBody))
                }
            } else {
                NetworkResponse.Error(PARSE_ERROR)
            }

        } else {
            response as NetworkResponse.Error
        }
    }

    companion object {
        private const val NATION_DEX_URL = "https://pokeapi.co/api/v2/pokemon?limit=2000"
        private const val PARSE_ERROR = "PARSE_ERROR"
    }
}