package com.widlof.minimaldex.nationaldex.data.interactor

import com.widlof.minimaldex.pokemondetails.data.model.PokemonStat
import com.widlof.minimaldex.pokemondetails.data.model.PokemonStatResponse

class GetStatsInteractor {
    fun getStats(stats: List<PokemonStatResponse>): List<PokemonStat> {
        val statList = mutableListOf<PokemonStat>()
        for (stat in stats) {
            statList.add(stat.stat.apply {
                value = stat.base_stat
            })
        }
        return statList
    }
}