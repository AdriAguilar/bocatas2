package com.example.bocatas2.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bocatas2.api.RetrofitConnect
import com.example.bocatas2.models.User
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    private val _errorMsg = MutableLiveData<String>()
    val errorMsg: LiveData<String> get() = _errorMsg

    fun fetchUsers() {

    }
}