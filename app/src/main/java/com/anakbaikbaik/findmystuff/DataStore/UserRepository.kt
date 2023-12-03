package com.anakbaikbaik.findmystuff.DataStore

import com.anakbaikbaik.findmystuff.Model.Session
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun saveUser(session: Session)
    suspend fun getUser() : Flow<Session>
}