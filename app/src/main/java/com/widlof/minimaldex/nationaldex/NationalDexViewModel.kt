package com.widlof.minimaldex.nationaldex

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.widlof.minimaldex.nationaldex.data.DexDataSource
import com.widlof.minimaldex.nationaldex.data.interactor.GetSinglePokemonInteractor
import com.widlof.minimaldex.nationaldex.data.model.PokemonListSingle
import com.widlof.minimaldex.pokemondetails.data.model.PokemonSingle
import com.widlof.minimaldex.ui.main.LoadingReasons
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class NationalDexViewModel(
    private val repository: DexDataSource,
    private val getSinglePokemonInteractor: GetSinglePokemonInteractor
) : ViewModel() {

    private val scope = MainScope()
    var pokemonList: MutableLiveData<List<PokemonListSingle>> = MutableLiveData()
    var pokemon: MutableLiveData<PokemonSingle> = MutableLiveData()
    private var error: String? = null
    val isLoadingEvolution: MutableLiveData<Boolean> = MutableLiveData()

    init {
        isLoadingEvolution.value = true
    }

    fun getPokemonList() {
        scope.launch {
            runCatching {
                repository.getNationalDex()
            }.onSuccess {
                pokemonList.value = it?.getPokemonList()
            }.onFailure {
                error = it.message
            }
        }
    }

    fun getSinglePokemon(name: String) {
        isLoadingEvolution.value = true
        scope.launch {
            val singlePokemonResponse = getSinglePokemonInteractor.getSinglePokemon(name)
            if (singlePokemonResponse != null) {
                pokemon.value = singlePokemonResponse
            } else {
                throw NullPointerException("Well your response is broken")
            }
        }
    }

    fun dexLoadComplete() {
        isLoadingEvolution.value = false
    }

    fun getLoadingReason(): String {
        return LoadingReasons.values()
            .toList()
            .shuffled()
            .first()
            .reason
    }
}