package com.widlof.minimaldex.nationaldex.data.model

data class NationalDexResponse(private val results: List<PokemonListSingle>?) {
    fun getPokemonList() = results
}