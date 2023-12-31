package com.anakbaikbaik.findmystuff.Data

import android.util.Log
import com.anakbaikbaik.findmystuff.DataStore.SessionData
import com.anakbaikbaik.findmystuff.Model.Session
import com.anakbaikbaik.findmystuff.Model.UserDatabase
import com.anakbaikbaik.findmystuff.Navigation.Screen
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.Objects
import javax.inject.Inject

class Authentication @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val sessionData: SessionData
) : AuthRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user
            val uid = user!!.uid
            val db = Firebase.firestore
            try{
                val userDocument = db.collection("users").document(uid).get().await()
                val userData = userDocument.toObject<UserDatabase>()
                if(userData != null){
                    sessionData.saveUser(
                        Session(
                            userId = user.uid,
                            name = userData.nama!!,
                            email = userData.email!!,
                            role = userData.role!!
                        )
                    )
                    Log.d("CHECK DATA", userDocument.toString())
                }
            }catch (e: Exception) {
                e.printStackTrace()
                Resource.Failure(e)
            }

            Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun signup(name: String, email: String, password: String): Resource<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()

            val user = result.user
            val db = Firebase.firestore
            val role = "0"

            try{
                if (user != null) {
                    db.collection("users").document(user.uid)
                        .set(
                            mapOf(
                                "nama" to name,
                                "email" to user.email,
                                "role" to role
                            )
                        )
                    sessionData.saveUser(
                        Session(
                            userId = user.uid,
                            name = name,
                            email = user.email!!,
                            role = role
                        )
                    )
                }
            } catch (e: Exception){
                Log.d("Error", e.toString())
            }

            result.user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())?.await()
            Resource.Success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override suspend fun forgetPassword(email: String): Resource<Void> {
        return try {
            // Use await() to wait for the completion of sendPasswordResetEmail
            firebaseAuth.sendPasswordResetEmail(email).await()
            Resource.Success(null) // Password reset successful
        } catch (e: FirebaseAuthInvalidUserException) {
            // Handle the case where the user does not exist
            e.printStackTrace()
            Resource.Failure(e)
        } catch (e: Exception) {
            // Handle other exceptions
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}