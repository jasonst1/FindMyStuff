package com.anakbaikbaik.findmystuff.DataStore

import com.anakbaikbaik.findmystuff.Model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun saveUser(user: User)
    fun getUser(): Flow<User?>
}