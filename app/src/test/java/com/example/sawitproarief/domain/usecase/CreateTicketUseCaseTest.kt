package com.example.sawitproarief.domain.usecase

import com.example.sawitproarief.domain.model.WeighbridgeTicket
import com.example.sawitproarief.domain.repository.WeighbridgeRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CreateTicketUseCaseTest {

    private lateinit var useCase: CreateTicketUseCase
    private val repository: WeighbridgeRepository = mockk(relaxed = true)

    private val sampleTicket = WeighbridgeTicket(
        id = "ticket1",
        driverName = "Arief Nugraha",
        netWeight = 1000.0,

    )

    @Before
    fun setup() {
        useCase = CreateTicketUseCase(repository)
    }

    @Test
    fun `execute should create ticket in repository`() = runTest {
        // Given
        coEvery { repository.createTicket(any()) } returns Unit

        // When
        useCase.doWork(CreateTicketUseCase.Param(sampleTicket))

        // Then
        coVerify { repository.createTicket(sampleTicket) }
    }

    @Test(expected = IllegalStateException::class)
    fun `execute should throw when repository fails`() = runTest {
        // Given
        coEvery { repository.createTicket(any()) } throws IllegalStateException()

        // When
        useCase.doWork(CreateTicketUseCase.Param(sampleTicket))
    }
}