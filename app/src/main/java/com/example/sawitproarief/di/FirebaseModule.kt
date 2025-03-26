package com.example.sawitproarief.di

import com.example.sawitproarief.data.local.WeighbridgeDao
import com.example.sawitproarief.data.remote.FirebaseWeighbridgeService
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseService(
        dao: WeighbridgeDao,
        database: FirebaseDatabase
    ): FirebaseWeighbridgeService {
        return FirebaseWeighbridgeService(dao, database)
    }
}