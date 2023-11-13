package com.anakbaikbaik.findmystuff.DI

import com.anakbaikbaik.findmystuff.Data.AuthRepository
import com.anakbaikbaik.findmystuff.Data.Authentication
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun providesAuthRepository(impl: Authentication): AuthRepository = impl
}