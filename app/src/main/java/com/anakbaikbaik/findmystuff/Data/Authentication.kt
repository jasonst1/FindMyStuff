package com.anakbaikbaik.findmystuff.Data

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class Authentication @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun signup(name: String, email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result.user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())?.await()
            Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun forgetPassword(email: String): Boolean {
        return try {
            // Use await() to wait for the completion of sendPasswordResetEmail
            firebaseAuth.sendPasswordResetEmail(email).await()
            Log.d("AUTHENTICATIONCONTROLLER","SUCCESS")
            true // Password reset successful
        } catch (e: FirebaseAuthInvalidUserException) {
            // Handle the case where the user does not exist
            e.printStackTrace()
            Log.d("AUTHENTICATIONCONTROLLER","FAIL 1")
            false // Password reset failed
        } catch (e: Exception) {
            // Handle other exceptions
            e.printStackTrace()
            Log.d("AUTHENTICATIONCONTROLLER","FAIL 2")
            false // Password reset failed
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }

}