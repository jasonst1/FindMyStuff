package com.anakbaikbaik.findmystuff.Data

import com.anakbaikbaik.findmystuff.Model.Session
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): Resource<FirebaseUser>
    suspend fun signup(name: String, email: String, password: String): Resource<FirebaseUser>
    suspend fun forgetPassword(email: String): Resource<Void>
    fun logout()
}