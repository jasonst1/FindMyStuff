package com.anakbaikbaik.findmystuff.DataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.anakbaikbaik.findmystuff.Data.AuthRepository
import com.anakbaikbaik.findmystuff.Model.Session
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

val Context.datastore: DataStore<Preferences> by preferencesDataStore(name="sessionStore")

class SessionData @Inject constructor(
    @ApplicationContext private val context: Context
) : UserRepository {
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

    override fun clearSessionData(){
        CoroutineScope(Dispatchers.IO).launch {
            context.datastore.edit { sessions ->
                sessions[email] = ""
                sessions[userId] = ""
                sessions[name] = ""
                sessions[role] = ""
            }
        }
    }

    companion object{
        val email = stringPreferencesKey("email")
        val name = stringPreferencesKey("name")
        val role = stringPreferencesKey("role")
        val userId = stringPreferencesKey("userId")
    }
}