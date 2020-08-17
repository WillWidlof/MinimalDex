package com.widlof.minimaldex.nationaldex

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.widlof.minimaldex.nationaldex.data.DexRemoteDataSource
import com.widlof.minimaldex.nationaldex.data.DexRepository
import com.widlof.minimaldex.nationaldex.data.model.PokemonListSingle
import com.widlof.minimaldex.network.NetworkRequestSender
import com.widlof.minimaldex.network.NetworkResponse
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class NationalDexViewModel : ViewModel() {

    private val scope = MainScope()
    var pokemonList: MutableLiveData<List<PokemonListSingle>> = MutableLiveData()
    private var error: String? = null

    fun getPokemonList() {
        scope.launch {
            val dexResponse =
                DexRepository(DexRemoteDataSource(NetworkRequestSender())).getNationalDex()
            when (dexResponse) {
                is NetworkResponse.Success -> {
                    pokemonList.value = dexResponse.responseBody?.getPokemonList()
                }
                is NetworkResponse.Error -> {
                    error = dexResponse.errorCode
                }
            }
        }
    }
}