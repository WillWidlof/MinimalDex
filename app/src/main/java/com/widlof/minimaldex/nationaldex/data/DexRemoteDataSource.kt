package com.widlof.minimaldex.nationaldex.data

import android.graphics.Bitmap
import android.util.Log
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.widlof.minimaldex.nationaldex.data.model.EvolutionChainResponse
import com.widlof.minimaldex.nationaldex.data.model.NationalDexResponse
import com.widlof.minimaldex.nationaldex.data.model.SpeciesResponse
import com.widlof.minimaldex.network.NetworkRequestSender
import com.widlof.minimaldex.network.NetworkResponse
import com.widlof.minimaldex.pokemondetails.data.model.PokemonResponse
import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class DexRemoteDataSource(
    private val dispatcher: CoroutineDispatcher,
    private val networkRequestSender: NetworkRequestSender
) : DexDataSource {

    private var scope: CoroutineScope = CoroutineScope(dispatcher)

    override suspend fun getNationalDex(): NetworkResponse<NationalDexResponse?> =
        suspendCoroutine { continuation ->
            scope.launch {
                networkRequestSender.makeJsonRequest(NATION_DEX_URL).apply {
                    if (this is NetworkResponse.Success) {
                        if (responseBody != null) {
                            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                            val adapter: JsonAdapter<NationalDexResponse> =
                                moshi.adapter(NationalDexResponse::class.java)
                            val response = adapter.fromJson(responseBody)
                            response?.getPokemonList()?.forEachIndexed { index, pokemonSingle ->
                                pokemonSingle.dexNo = (index + 1).toString()
                            }
                            continuation.resume(NetworkResponse.Success(response))
                        } else {
                            continuation.resume(NetworkResponse.Error(PARSE_ERROR))
                        }
                    } else if (this is NetworkResponse.Error) {
                        continuation.resume(NetworkResponse.Error(this.errorCode))
                    }
                }
            }
        }

    override suspend fun getSinglePokemonMainJson(name: String): NetworkResponse<PokemonResponse?> =
        suspendCoroutine { continuation ->
            scope.launch {
                val url = POKEMON_URL + name.toLowerCase()
                val response = networkRequestSender.makeJsonRequest(url)
                if (response is NetworkResponse.Success) {
                    if (response.responseBody != null) {
                        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                        val adapter: JsonAdapter<PokemonResponse> =
                            moshi.adapter(PokemonResponse::class.java)
                        val result = adapter.fromJson(response.responseBody)
                        continuation.resume(NetworkResponse.Success(result))
                    } else {
                        continuation.resume(NetworkResponse.Error(PARSE_ERROR))
                    }
                } else {
                    response as NetworkResponse.Error
                    continuation.resume(NetworkResponse.Error(response.errorCode))
                }
            }
        }

    override suspend fun getSprite(spriteUrl: String): NetworkResponse<Bitmap?> =
        withContext(Dispatchers.IO) {
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

    override suspend fun getSpeciesBase(url: String): NetworkResponse<SpeciesResponse?> =
        withContext(Dispatchers.IO) {
            val response = networkRequestSender.makeJsonRequest(url)
            return@withContext if (response is NetworkResponse.Success) {
                if (response.responseBody != null) {
                    response.responseBody.let {
                        Log.d("JSON RESPONSE:", response.responseBody)
                        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                        val adapter: JsonAdapter<SpeciesResponse> =
                            moshi.adapter(SpeciesResponse::class.java)
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

    override suspend fun getEvolutionChain(url: String):
            NetworkResponse<EvolutionChainResponse?> = withContext(Dispatchers.IO) {
        val response = networkRequestSender.makeJsonRequest(url)
        return@withContext if (response is NetworkResponse.Success) {
            if (response.responseBody != null) {
                response.responseBody.let {
                    Log.d("JSON RESPONSE:", response.responseBody)
                    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                    val adapter: JsonAdapter<EvolutionChainResponse> =
                        moshi.adapter(EvolutionChainResponse::class.java)
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

    companion object {
        private const val POKEMON_URL = "https://pokeapi.co/api/v2/pokemon/"
        private const val NATION_DEX_URL = "https://pokeapi.co/api/v2/pokemon?limit=2000"
        private const val PARSE_ERROR = "PARSE_ERROR"
    }
}