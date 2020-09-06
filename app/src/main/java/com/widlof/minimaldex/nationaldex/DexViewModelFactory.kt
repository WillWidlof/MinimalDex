@file:Suppress("UNCHECKED_CAST")

package com.widlof.minimaldex.nationaldex

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.widlof.minimaldex.nationaldex.data.DexRemoteDataSource
import com.widlof.minimaldex.nationaldex.data.DexRepository
import com.widlof.minimaldex.nationaldex.data.interactor.GetSinglePokemonInteractor
import com.widlof.minimaldex.network.NetworkClientBuilder
import com.widlof.minimaldex.network.NetworkRequestBuilder
import com.widlof.minimaldex.network.NetworkRequestSender
import com.widlof.minimaldex.pokemondetails.PokemonDetailsViewModel
import kotlinx.coroutines.Dispatchers
import java.lang.ClassCastException

class DexViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(NationalDexViewModel::class.java)) {
            val remoteDataSource = DexRemoteDataSource(Dispatchers.IO,
                NetworkRequestSender(Dispatchers.IO, NetworkRequestBuilder(), NetworkClientBuilder()))
            val repository = DexRepository(remoteDataSource)
            val getSinglePokemonInteractor = GetSinglePokemonInteractor.newInstance()
            NationalDexViewModel(repository, getSinglePokemonInteractor) as T
        } else if (modelClass.isAssignableFrom(PokemonDetailsViewModel::class.java)) {
            PokemonDetailsViewModel(GetSinglePokemonInteractor.newInstance()) as T
        } else {
            throw ClassCastException("No matching view model found")
        }
    }
}