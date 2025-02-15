package com.example.bocatas2.api

import com.example.bocatas2.models.User
import retrofit2.http.GET

interface ApiServiceUser {

    @GET("users")
    suspend fun getUsers(): List<User>
}