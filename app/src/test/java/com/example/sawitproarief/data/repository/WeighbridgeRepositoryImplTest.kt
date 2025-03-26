package com.example.sawitproarief.data.repository

import com.example.sawitproarief.data.local.WeighbridgeDao
import com.example.sawitproarief.data.local.entity.SyncStatus
import com.example.sawitproarief.data.local.entity.WeighbridgeTicketEntity
import com.example.sawitproarief.data.remote.FirebaseWeighbridgeService
import com.example.sawitproarief.domain.model.WeighbridgeTicket
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class WeighbridgeRepositoryImplTest {

    private lateinit var repository: WeighbridgeRepositoryImpl
    private val localDataSource: WeighbridgeDao = mockk(relaxed = true)
    private val remoteDataSource: FirebaseWeighbridgeService = mockk(relaxed = true)

    private val sampleTicket = WeighbridgeTicket(
        id = "ticket1",
        driverName = "Arief Nugraha",
        netWeight = 1000.0,
        dateTime = 4326,
        licenseNumber = "civibus",
        inboundWeight = 4.5,
        outboundWeight = 6.7,
    )

    private val sampleEntity = WeighbridgeTicketEntity(
        id = "ticket1",
        driverName = "Arief Nugraha",
        netWeight = 1000.0,
        syncStatus = SyncStatus.PENDING,
        dateTime = 4326,
        licenseNumber = "civibus",
        inboundWeight = 4.5,
        outboundWeight = 6.7,

    )

    @Before
    fun setup() {
        repository = WeighbridgeRepositoryImpl(localDataSource, remoteDataSource)
    }

    @Test
    fun `createTicket should save to local and remote`() = runTest {
        // Given
        coEvery { localDataSource.upsertTicket(any()) } returns Unit
        coEvery { remoteDataSource.createTicket(any()) } returns Unit

        // When
        repository.createTicket(sampleTicket)

        // Then
        coVerify {
            localDataSource.upsertTicket(match { it.syncStatus == SyncStatus.PENDING })
            remoteDataSource.createTicket(sampleTicket)
            localDataSource.upsertTicket(match { it.syncStatus == SyncStatus.SYNCED })
        }
    }

    @Test
    fun `updateTicket should update local and remote`() = runTest {
        // Given
        coEvery { localDataSource.upsertTicket(any()) } returns Unit
        coEvery { remoteDataSource.updateTicket(any()) } returns Unit

        // When
        repository.updateTicket(sampleTicket)

        // Then
        coVerify {
            localDataSource.upsertTicket(match { it.syncStatus == SyncStatus.PENDING })
            remoteDataSource.updateTicket(sampleTicket)
            localDataSource.upsertTicket(match { it.syncStatus == SyncStatus.SYNCED })
        }
    }

    @Test
    fun `deleteTicket should delete from local and remote`() = runTest {
        // Given
        coEvery { localDataSource.getTicketById(any()) } returns sampleEntity
        coEvery { localDataSource.deleteTicketById(any()) } returns Unit
        coEvery { remoteDataSource.deleteTicket(any()) } returns Unit

        // When
        repository.deleteTicket("ticket1")

        // Then
        coVerify {
            localDataSource.deleteTicketById(any())
            remoteDataSource.deleteTicket("ticket1")
        }
    }



    @Test
    fun `getTicket should return ticket by id`() = runTest {
        // Given
        coEvery { localDataSource.getTicketById(any()) } returns sampleEntity

        // When
        val result = repository.getTicket("ticket1")

        // Then
        assertEquals(sampleTicket.id, result.id)
        assertEquals(sampleTicket.licenseNumber, result.licenseNumber)
    }
}