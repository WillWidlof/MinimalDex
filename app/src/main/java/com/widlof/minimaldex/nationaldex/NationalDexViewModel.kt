package com.widlof.minimaldex.nationaldex

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.widlof.minimaldex.nationaldex.data.DexDataSource
import com.widlof.minimaldex.nationaldex.data.GetSinglePokemonInteractor
import com.widlof.minimaldex.nationaldex.data.model.PokemonListSingle
import com.widlof.minimaldex.pokemondetails.data.model.PokemonSingle
import com.widlof.minimaldex.network.NetworkResponse
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.lang.NullPointerException

class NationalDexViewModel(private val repository: DexDataSource,
                           private val getSinglePokemonInteractor: GetSinglePokemonInteractor)
    : ViewModel() {

    private val scope = MainScope()
    var pokemonList: MutableLiveData<List<PokemonListSingle>> = MutableLiveData()
    var pokemon: MutableLiveData<PokemonSingle> = MutableLiveData()
    private var error: String? = null

    fun getPokemonList() {
        scope.launch {
            val dexResponse =
                repository.getNationalDex()
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

    fun getSinglePokemon(name: String) {
        scope.launch {
            val singlePokemonResponse = getSinglePokemonInteractor.getSinglePokemon(name)
            if (singlePokemonResponse != null) {
                pokemon.value = singlePokemonResponse
            } else {
                throw NullPointerException("Well your response is broken")
            }
        }
    }
}