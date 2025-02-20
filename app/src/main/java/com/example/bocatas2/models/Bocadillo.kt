package com.example.bocatas2.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Bocadillo(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val tipo: String,
    val coste: Double,
    val dia: String
): Parcelable {
    constructor() : this("", "", "", "", 0.0, "")
}
