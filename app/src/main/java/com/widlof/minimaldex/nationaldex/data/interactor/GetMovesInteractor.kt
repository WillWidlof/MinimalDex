package com.widlof.minimaldex.nationaldex.data.interactor

import com.widlof.minimaldex.pokemondetails.data.model.PokemonMove
import com.widlof.minimaldex.pokemondetails.data.model.PokemonMoveResponse

class GetMovesInteractor {
    fun getMoves(moves: List<PokemonMoveResponse>): List<PokemonMove> {
        val moveList = mutableListOf<PokemonMove>()
        for (move in moves) {
            moveList.add(move.move)
        }
        return moveList
    }
}