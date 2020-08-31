package com.widlof.minimaldex.util

import android.view.View
import android.view.animation.OvershootInterpolator

import androidx.core.view.ViewCompat




class ViewUtils {
    companion object {
        fun rotateFabForward(view: View) {
            ViewCompat.animate(view)
                .rotation(135.0f)
                .withLayer()
                .setDuration(300L)
                .setInterpolator(OvershootInterpolator(10.0f))
                .start()
        }

        fun rotateFabBackward(view: View) {
            ViewCompat.animate(view)
                .rotation(0.0f)
                .withLayer()
                .setDuration(300L)
                .setInterpolator(OvershootInterpolator(10.0f))
                .start()
        }
    }
}