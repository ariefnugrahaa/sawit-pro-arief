package com.example.sawitproarief.di

import android.content.Context
import androidx.room.Room
import com.example.sawitproarief.data.local.WeighbridgeDao
import com.example.sawitproarief.data.local.WeighbridgeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): WeighbridgeDatabase {
        return Room.databaseBuilder(
            context,
            WeighbridgeDatabase::class.java,
            "weighbridge.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideWeighbridgeDao(database: WeighbridgeDatabase): WeighbridgeDao {
        return database.weighbridgeDao()
    }
}