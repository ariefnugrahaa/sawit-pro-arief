package com.example.sawitproarief.domain.usecase

import com.example.sawitproarief.domain.model.WeighbridgeTicket
import com.example.sawitproarief.domain.repository.WeighbridgeRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetTicketUseCaseTest {

    private lateinit var useCase: GetTicketUseCase
    private val repository: WeighbridgeRepository = mockk(relaxed = true)

    private val sampleTicket = WeighbridgeTicket(
        id = "ticket1",
        driverName = "Arief Nugraha",
        netWeight = 1000.0,
        dateTime = 8187,
        licenseNumber = "verear",
        inboundWeight = 20.21,
        outboundWeight = 22.23,
    )

    @Before
    fun setup() {
        useCase = GetTicketUseCase(repository)
    }

    @Test
    fun `execute should return flow of ticket`() = runTest {
        // Given
        coEvery { repository.getTicketFlow(any()) } returns flowOf(sampleTicket)

        // When
        val result = useCase.doWork(GetTicketUseCase.Param("ticket1"))

        // Then
        result.collect { ticket ->
            assertEquals(sampleTicket.id, ticket.id)
            assertEquals(sampleTicket.licenseNumber, ticket.licenseNumber)
            assertEquals(sampleTicket.driverName, ticket.driverName)
            assertEquals(sampleTicket.netWeight, ticket.netWeight, 0.01)
        }
    }

    @Test(expected = IllegalStateException::class)
    fun `execute should throw when repository fails`() = runTest {
        // Given
        coEvery { repository.getTicketFlow(any()) } throws IllegalStateException()

        // When
        useCase.doWork(GetTicketUseCase.Param("ticket1"))
    }
}