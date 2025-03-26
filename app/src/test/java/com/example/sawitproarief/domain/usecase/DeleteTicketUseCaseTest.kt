package com.example.sawitproarief.domain.usecase

import com.example.sawitproarief.domain.repository.WeighbridgeRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class DeleteTicketUseCaseTest {

    private lateinit var useCase: DeleteTicketUseCase
    private val repository: WeighbridgeRepository = mockk(relaxed = true)

    @Before
    fun setup() {
        useCase = DeleteTicketUseCase(repository)
    }

    @Test
    fun `execute should delete ticket from repository`() = runTest {
        // Given
        val ticketId = "ticket1"
        coEvery { repository.deleteTicket(any()) } returns Unit

        // When
        useCase.doWork(DeleteTicketUseCase.Param(ticketId))

        // Then
        coVerify { repository.deleteTicket(ticketId) }
    }

    @Test(expected = IllegalStateException::class)
    fun `execute should throw when repository fails`() = runTest {
        // Given
        val ticketId = "ticket1"
        coEvery { repository.deleteTicket(any()) } throws IllegalStateException()

        // When
        useCase.doWork(DeleteTicketUseCase.Param(ticketId))
    }
} 