package com.example.bocatas2.models

data class Pedido(
    val user_id: String,
    val bocadillo_id: String,
    val coste_total: Double,
    val fecha: String
)
