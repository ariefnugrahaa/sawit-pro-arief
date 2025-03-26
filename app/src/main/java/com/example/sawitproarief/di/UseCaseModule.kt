package com.example.sawitproarief.di

import com.example.sawitproarief.domain.repository.WeighbridgeRepository
import com.example.sawitproarief.domain.usecase.CreateTicketUseCase
import com.example.sawitproarief.domain.usecase.DeleteTicketUseCase
import com.example.sawitproarief.domain.usecase.GetAllTicketsUseCase
import com.example.sawitproarief.domain.usecase.GetTicketUseCase
import com.example.sawitproarief.domain.usecase.UpdateTicketUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideCreateTicketUseCase(repository: WeighbridgeRepository): CreateTicketUseCase {
        return CreateTicketUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideUpdateTicketUseCase(repository: WeighbridgeRepository): UpdateTicketUseCase {
        return UpdateTicketUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetTicketUseCase(repository: WeighbridgeRepository): GetTicketUseCase {
        return GetTicketUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetAllTicketsUseCase(repository: WeighbridgeRepository): GetAllTicketsUseCase {
        return GetAllTicketsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteTicketUseCase(repository: WeighbridgeRepository): DeleteTicketUseCase {
        return DeleteTicketUseCase(repository)
    }
}