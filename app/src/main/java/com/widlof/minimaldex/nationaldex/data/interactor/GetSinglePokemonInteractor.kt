package com.widlof.minimaldex.nationaldex.data.interactor

import com.widlof.minimaldex.nationaldex.data.DexDataSource
import com.widlof.minimaldex.nationaldex.data.DexRemoteDataSource
import com.widlof.minimaldex.nationaldex.data.DexRepository
import com.widlof.minimaldex.network.NetworkClientBuilder
import com.widlof.minimaldex.network.NetworkRequestBuilder
import com.widlof.minimaldex.network.NetworkRequestSender
import com.widlof.minimaldex.pokemondetails.data.PokemonCache
import com.widlof.minimaldex.pokemondetails.data.model.PokemonSingle
import kotlinx.coroutines.Dispatchers

class GetSinglePokemonInteractor(
    private val repository: DexDataSource,
    private val getSpriteInteractor: GetSpriteInteractor,
    private val getTypesInteractor: GetTypesInteractor,
    private val getMovesInteractor: GetMovesInteractor,
    private val getStatsInteractor: GetStatsInteractor,
    private val getSpeciesInteractor: GetSpeciesInteractor
) {
    suspend fun getSinglePokemon(name: String): PokemonSingle? {
        PokemonCache.pokemonCache[name]?.let {
            return it
        }
        val response = repository.getSinglePokemonMainJson(name)
        response?.let {
            val frontSprite = getSpriteInteractor.getSprite(it.sprites.front_default)
            val backSprite = getSpriteInteractor.getSprite(it.sprites.back_default)
            val types = getTypesInteractor.getTypes(it.types)
            val moves = getMovesInteractor.getMoves(it.moves)
            val stats = getStatsInteractor.getStats(it.stats)
            val species = getSpeciesInteractor.getSpecies(it.species)

            with(response) {
                val pokemon = PokemonSingle(
                    name,
                    frontSprite,
                    backSprite,
                    sprites,
                    moves,
                    stats,
                    types,
                    species
                )
                PokemonCache.pokemonCache[name] = pokemon
                return PokemonCache.pokemonCache[name]
            }
        }
        return null
    }

    companion object {
        fun newInstance(): GetSinglePokemonInteractor {
            val dispatcher = Dispatchers.IO
            val networkRequestSender = NetworkRequestSender(
                dispatcher, NetworkRequestBuilder(), NetworkClientBuilder()
            )
            val dataSource = DexRemoteDataSource(dispatcher, networkRequestSender)
            val repository = DexRepository(dataSource)
            return GetSinglePokemonInteractor(
                repository,
                GetSpriteInteractor(repository),
                GetTypesInteractor(),
                GetMovesInteractor(),
                GetStatsInteractor(),
                GetSpeciesInteractor(repository, GetEvolutionListInteractor())
            )
        }
    }

}