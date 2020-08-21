package com.widlof.minimaldex.nationaldex.data

import android.graphics.Bitmap
import android.util.Log
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.widlof.minimaldex.nationaldex.data.model.NationalDexResponse
import com.widlof.minimaldex.pokemondetails.data.model.PokemonResponse
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
                    val response = adapter.fromJson(response.responseBody)
                    response?.results?.forEachIndexed { index, pokemonSingle ->
                        pokemonSingle.dexNo = (index + 1).toString()
                    }
                    NetworkResponse.Success(response)
                }
            } else {
                NetworkResponse.Error(PARSE_ERROR)
            }

        } else {
            response as NetworkResponse.Error
        }
    }

    override suspend fun getSinglePokemonMainJson(name: String): NetworkResponse<PokemonResponse?> = withContext(Dispatchers.IO) {
        val url = POKEMON_URL + name.toLowerCase()
        val response = networkRequestSender.makeJsonRequest(url)
        return@withContext if (response is NetworkResponse.Success) {
            if (response.responseBody != null) {
                response.responseBody.let {
                    Log.d("JSON RESPONSE:", response.responseBody)
                    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                    val adapter: JsonAdapter<PokemonResponse>
                            = moshi.adapter(PokemonResponse::class.java)
                    val result = adapter.fromJson(response.responseBody)
                    NetworkResponse.Success(result)
                }
            } else {
                NetworkResponse.Error(PARSE_ERROR)
            }
        } else {
            response as NetworkResponse.Error
        }
    }

    override suspend fun getSprite(spriteUrl: String): NetworkResponse<Bitmap?>
            = withContext(Dispatchers.IO) {
        val response = networkRequestSender.makeImageRequest(spriteUrl)
        return@withContext if (response is NetworkResponse.Success) {
            if (response.responseBody != null) {
                response.responseBody.let {
                    NetworkResponse.Success(it)
                }
            } else {
                NetworkResponse.Error(PARSE_ERROR)
            }
        } else {
            response as NetworkResponse.Error
        }
    }

    companion object {
        private const val POKEMON_URL = "https://pokeapi.co/api/v2/pokemon/"
        private const val NATION_DEX_URL = "https://pokeapi.co/api/v2/pokemon?limit=2000"
        private const val PARSE_ERROR = "PARSE_ERROR"
    }
}