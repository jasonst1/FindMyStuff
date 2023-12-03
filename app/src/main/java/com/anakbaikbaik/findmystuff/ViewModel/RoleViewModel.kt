package com.anakbaikbaik.findmystuff.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anakbaikbaik.findmystuff.Data.AuthRepository
import com.anakbaikbaik.findmystuff.DataStore.SessionData
import com.anakbaikbaik.findmystuff.DataStore.UserRepository
import com.anakbaikbaik.findmystuff.Model.Session
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoleViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    private val _role= MutableStateFlow<String>("")
    val role: StateFlow<String> get() = _role

    private val _email = MutableStateFlow<String>("")
    val email: StateFlow<String> get() = _email

    private val _name= MutableStateFlow<String>("")
    val name: StateFlow<String> get() = _name

    private val _userId= MutableStateFlow<String>("")
    val userId: StateFlow<String> get() = _userId

    private val _currentSession: MutableStateFlow<Session?> = MutableStateFlow(null)
    val currentSession: StateFlow<Session?> get() = _currentSession

    fun saveData(userId: String, name: String, email: String, role: String) {
        viewModelScope.launch {
            repository.saveUser(
                Session(
                    _userId.value,
                    _name.value,
                    _email.value,
                    _role.value
                ).apply {
                    // Now update the properties with the new values
                    _userId.value = userId
                    _name.value = name
                    _email.value = email
                    _role.value = role
                }
            )
        }
    }

    fun retrieveData(){
        viewModelScope.launch {
            repository.getUser().collect {session ->
                _currentSession.value = session
            }
        }
    }
}