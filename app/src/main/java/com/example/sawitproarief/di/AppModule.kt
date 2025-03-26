package com.example.sawitproarief.di

import com.example.sawitproarief.data.local.WeighbridgeDao
import com.example.sawitproarief.data.remote.FirebaseWeighbridgeService
import com.example.sawitproarief.data.repository.WeighbridgeRepositoryImpl
import com.example.sawitproarief.domain.repository.WeighbridgeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideWeighbridgeRepository(
        dao: WeighbridgeDao,
        firebaseService: FirebaseWeighbridgeService
    ): WeighbridgeRepository {
        return WeighbridgeRepositoryImpl(dao, firebaseService)
    }
}