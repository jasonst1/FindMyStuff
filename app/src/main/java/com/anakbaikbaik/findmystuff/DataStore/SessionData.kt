package com.anakbaikbaik.findmystuff.DataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.anakbaikbaik.findmystuff.Model.Session
import kotlinx.coroutines.flow.map

val Context.datastore: DataStore<Preferences> by preferencesDataStore(name="sessionStore")

class SessionData(private val context: Context) : UserRepository {
    override suspend fun saveUser(session: Session) {
        context.datastore.edit { sessions ->
            sessions[email] = session.email
            sessions[userId] = session.userId
            sessions[name] = session.name
            sessions[role] = session.role
        }
    }

    override suspend fun getUser() = context.datastore.data.map { session ->
        Session(
            userId = session[userId]!!,
            name = session[name]!!,
            email = session[email]!!,
            role = session[role]!!
        )
    }

    companion object{
        val email = stringPreferencesKey("email")
        val name = stringPreferencesKey("name")
        val role = stringPreferencesKey("role")
        val userId = stringPreferencesKey("userId")
    }
}