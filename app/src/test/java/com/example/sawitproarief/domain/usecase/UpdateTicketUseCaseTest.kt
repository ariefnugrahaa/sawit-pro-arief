package com.example.sawitproarief.domain.usecase

import com.example.sawitproarief.domain.model.WeighbridgeTicket
import com.example.sawitproarief.domain.repository.WeighbridgeRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UpdateTicketUseCaseTest {

    private lateinit var useCase: UpdateTicketUseCase
    private val repository: WeighbridgeRepository = mockk(relaxed = true)

    private val sampleTicket = WeighbridgeTicket(
        id = "ticket1",
        driverName = "Arief Nugraha",
        netWeight = 1000.0,
        dateTime = 8187,
        licenseNumber = "AB1234CD",
        inboundWeight = 3000.0,
        outboundWeight = 2000.0,
    )

    @Before
    fun setup() {
        useCase = UpdateTicketUseCase(repository)
    }

    @Test
    fun `execute should update ticket in repository`() = runTest {
        // Given
        coEvery { repository.updateTicket(any()) } returns Unit

        // When
        useCase.doWork(UpdateTicketUseCase.Param(sampleTicket))

        // Then
        coVerify { repository.updateTicket(sampleTicket) }
    }

    @Test(expected = IllegalStateException::class)
    fun `execute should throw when repository fails`() = runTest {
        // Given
        coEvery { repository.updateTicket(any()) } throws IllegalStateException()

        // When
        useCase.doWork(UpdateTicketUseCase.Param(sampleTicket))
    }
} 