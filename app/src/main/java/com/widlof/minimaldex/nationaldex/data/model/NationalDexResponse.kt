package com.widlof.minimaldex.nationaldex.data.model

data class NationalDexResponse(val results: List<PokemonListSingle>?) {
    fun getPokemonList() = results
}