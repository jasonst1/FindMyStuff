package com.anakbaikbaik.findmystuff.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anakbaikbaik.findmystuff.Data.AuthRepository
import com.anakbaikbaik.findmystuff.DataStore.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class RoleViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    private val _role: MutableStateFlow<String> = MutableStateFlow("")
    val role: StateFlow<String> get() = _role

    private val _name: MutableStateFlow<String> = MutableStateFlow("")
    val name: StateFlow<String> get() = _name

    private val _email: MutableStateFlow<String> = MutableStateFlow("")
    val email: StateFlow<String> get() = _email

    private val _userId: MutableStateFlow<String> = MutableStateFlow("")
    val userId: StateFlow<String> get() = _userId

    fun saveData(){

    }

    fun retrieveData(){
        
    }
}