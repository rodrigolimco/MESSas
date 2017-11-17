package com.example.rodrigo.messas.model

import java.io.Serializable


object Plates: Serializable {
    var plates: List<Plate> = listOf(
            Plate("Macarrones"),
            Plate("Pizza"),
            Plate("Paella"),
            Plate("Hamburguesa")
    )

    val count
        get() = plates.size

    fun toArray() = plates.toTypedArray()
}