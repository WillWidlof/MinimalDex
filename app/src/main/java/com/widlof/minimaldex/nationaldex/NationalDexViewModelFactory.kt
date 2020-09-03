@file:Suppress("UNCHECKED_CAST")

package com.widlof.minimaldex.nationaldex

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.widlof.minimaldex.nationaldex.data.DexRemoteDataSource
import com.widlof.minimaldex.nationaldex.data.DexRepository
import com.widlof.minimaldex.nationaldex.data.GetSinglePokemonInteractor
import com.widlof.minimaldex.network.NetworkRequestSender
import kotlinx.coroutines.Dispatchers
import java.lang.ClassCastException

class NationalDexViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(NationalDexViewModel::class.java)) {
            val remoteDataSource = DexRemoteDataSource(Dispatchers.IO, NetworkRequestSender())
            val repository = DexRepository(remoteDataSource)
            val getSinglePokemonInteractor = GetSinglePokemonInteractor(repository)
            NationalDexViewModel(repository, getSinglePokemonInteractor) as T
        } else {
            throw ClassCastException("No matching view model found")
        }
    }
}