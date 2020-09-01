package com.widlof.minimaldex.pokemondetails

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.core.graphics.toColor
import com.widlof.minimaldex.R
import com.widlof.minimaldex.util.DrawableUtils

class StatBarBackground(private val context: Context) {

    fun provideBarColour(statName: String): Int {
        return when(statName) {
            HP -> R.color.hp
            ATTACK -> R.color.attack
            DEFENSE -> R.color.defense
            SPECIAL_ATTACK -> R.color.special_attack
            SPECIAL_DEFENSE -> R.color.special_defense
            SPEED -> R.color.speed
            else -> R.color.default_stat
        }
    }

    companion object {
        private const val HP = "hp"
        private const val ATTACK = "attack"
        private const val DEFENSE = "defense"
        private const val SPECIAL_ATTACK = "special-attack"
        private const val SPECIAL_DEFENSE = "special-defense"
        private const val SPEED = "speed"
    }
}