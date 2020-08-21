package com.widlof.minimaldex.nationaldex

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.widlof.minimaldex.R
import com.widlof.minimaldex.nationaldex.data.model.PokemonListSingle
import kotlinx.android.synthetic.main.national_dex.view.*
import java.util.*

class NationalDexAdapter(private val context: Context,
                         var pokemonList: List<PokemonListSingle>,
                         var nationalDexListener: NationalDexListener)
    : RecyclerView.Adapter<NationalDexAdapter.ViewHolder>() {

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        internal var pokemonName = itemView.tv_pokemon_name
        internal var pokemonNumber = itemView.tv_pokemon_number
        internal var layout = itemView.cl_pokemon
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.national_dex, null))
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pokemon = pokemonList[position]
        holder.pokemonName.text = pokemon.name.substring(0, 1).toUpperCase(Locale.ROOT) + pokemon.name.substring(1);
        val pokemonNumber = String.format("%04d", (pokemon.dexNo?.toInt()))
        holder.pokemonNumber.text = "#$pokemonNumber"
        holder.layout.setOnClickListener {
            nationalDexListener.onPokemonClicked(pokemon)
        }
    }
}