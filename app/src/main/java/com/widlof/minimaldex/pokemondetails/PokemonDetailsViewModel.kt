package com.widlof.minimaldex.pokemondetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.widlof.minimaldex.nationaldex.data.interactor.GetSinglePokemonInteractor
import com.widlof.minimaldex.pokemondetails.data.PokemonCache
import com.widlof.minimaldex.pokemondetails.data.model.PokemonSingle
import com.widlof.minimaldex.ui.main.LoadingReasons
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class PokemonDetailsViewModel(private val getSinglePokemonInteractor: GetSinglePokemonInteractor) : ViewModel() {

    private val scope = MainScope()
    val pokemon: MutableLiveData<PokemonSingle> = MutableLiveData()
    val isLoadingEvolution: MutableLiveData<Boolean> = MutableLiveData()

    init {
        isLoadingEvolution.value = false
    }

    fun findPokemon(pokemonName: String) {
        pokemon.value = PokemonCache.pokemonCache[pokemonName]
    }

    fun getPokemonEvolvedForm(pokemonName: String) {
        isLoadingEvolution.value = true
        if (PokemonCache.pokemonCache.containsKey(pokemonName)) {
            pokemon.value = PokemonCache.pokemonCache[pokemonName]
        } else {
            //Snackbar?
            scope.launch {
                runCatching {
                    getSinglePokemonInteractor.getSinglePokemon(pokemonName)
                }.onSuccess {
                    pokemon.value = it
                }.onFailure {
                    //Not needed displayed Pokemon will remain the same
                }
            }
        }
    }

    fun loadingEvolutionComplete() {
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