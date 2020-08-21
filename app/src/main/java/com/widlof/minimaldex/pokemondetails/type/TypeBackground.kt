package com.widlof.minimaldex.pokemondetails.type

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RoundRectShape
import com.widlof.minimaldex.R


class TypeBackground(private val context: Context) {
        fun provideBackground(type: String): Drawable {
            return generateDrawable(when(type) {
                BUG -> R.color.bug
                DARK -> R.color.dark
                DRAGON -> R.color.dragon
                ELECTRIC -> R.color.electric
                FAIRY -> R.color.fairy
                FIGHTING -> R.color.fighting
                FIRE -> R.color.fire
                FLYING -> R.color.flying
                GHOST -> R.color.ghost
                GRASS -> R.color.grass
                GROUND -> R.color.ground
                ICE -> R.color.ice
                NORMAL -> R.color.normal
                POISON -> R.color.poison
                PSYCHIC -> R.color.psychic
                ROCK -> R.color.rock
                STEEL -> R.color.steel
                WATER -> R.color.water
                else -> R.color.unknown
            })
        }

        private fun generateDrawable(color: Int): Drawable {
            val r = 8f
            return ShapeDrawable(
                RoundRectShape(floatArrayOf(r, r, r, r, r, r, r, r), null, null)).apply {
                paint.color = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    context.getColor(color)
                } else {
                    context.resources.getColor(color)
                }
            }
        }

    companion object {
        private const val BUG = "bug"
        private const val DARK = "dark"
        private const val DRAGON = "dragon"
        private const val ELECTRIC = "electric"
        private const val FAIRY = "fairy"
        private const val FIRE = "fire"
        private const val FIGHTING = "fighting"
        private const val FLYING = "flying"
        private const val GHOST = "ghost"
        private const val GRASS = "grass"
        private const val GROUND = "ground"
        private const val ICE = "ice"
        private const val NORMAL = "normal"
        private const val POISON = "psychic"
        private const val PSYCHIC = "poison"
        private const val ROCK = "rock"
        private const val STEEL = "steel"
        private const val WATER = "water"
    }
}