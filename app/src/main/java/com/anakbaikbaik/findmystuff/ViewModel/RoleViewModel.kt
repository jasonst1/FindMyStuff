package com.anakbaikbaik.findmystuff.ViewModel

import androidx.lifecycle.ViewModel
import com.anakbaikbaik.findmystuff.Data.AuthRepository
import com.anakbaikbaik.findmystuff.DataStore.UserRepository
import javax.inject.Inject

class RoleViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

}