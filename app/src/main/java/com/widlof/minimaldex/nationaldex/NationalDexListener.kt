package com.widlof.minimaldex.nationaldex

import com.widlof.minimaldex.nationaldex.data.model.PokemonListSingle

interface NationalDexListener {
    fun onPokemonClicked(pokemon: PokemonListSingle)
}