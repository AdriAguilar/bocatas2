package com.example.bocatas2.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bocatas2.models.User

class UserViewModel : ViewModel() {
    private val _listUsers = MutableLiveData<List<User>>()
    val listUsers: LiveData<List<User>> get() = _listUsers

    fun fetchUsers() {

    }
}