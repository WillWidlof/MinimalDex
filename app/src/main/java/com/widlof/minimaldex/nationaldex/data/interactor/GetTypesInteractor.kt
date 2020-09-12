package com.widlof.minimaldex.nationaldex.data.interactor

import com.widlof.minimaldex.pokemondetails.data.model.PokemonType
import com.widlof.minimaldex.pokemondetails.data.model.PokemonTypeResponse

class GetTypesInteractor {
    fun getTypes(types: List<PokemonTypeResponse>): List<PokemonType> {
        val typeList = mutableListOf<PokemonType>()
        for (type in types) {
            with(type.type) {
                typeList.add(PokemonType(name = name, url = url))
            }
        }
        return typeList
    }
}