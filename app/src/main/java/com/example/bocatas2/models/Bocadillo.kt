package com.example.bocatas2.models

data class Bocadillo(
    val id: String,
    val nombre: String,
    val descripcion: String,
    val tipo: String,
    val coste: Double,
    val dia: String
) {
    constructor() : this("", "", "", "", 0.0, "")
}
