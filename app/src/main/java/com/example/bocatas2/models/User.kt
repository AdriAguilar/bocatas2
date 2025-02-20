package com.example.bocatas2.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    val id: String,
    val name: String,
    val email: String,
    val role: String
): Parcelable {
    constructor() : this("", "", "", "")
}