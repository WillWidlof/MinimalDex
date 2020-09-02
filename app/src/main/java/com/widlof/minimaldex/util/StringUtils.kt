package com.widlof.minimaldex.util

class StringUtils {
    companion object {
        fun String.capitaliseAll(): String {
            return this.replace("-", " ")
                .split(' ')
                .joinToString(" ") {
                        it -> it.capitalize()
                }
        }
    }
}