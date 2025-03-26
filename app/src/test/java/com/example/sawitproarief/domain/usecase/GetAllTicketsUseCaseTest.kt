package com.example.sawitproarief.domain.usecase

import com.example.sawitproarief.domain.model.WeighbridgeTicket
import com.example.sawitproarief.domain.repository.WeighbridgeRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetAllTicketsUseCaseTest {

    private lateinit var useCase: GetAllTicketsUseCase
    private val repository: WeighbridgeRepository = mockk(relaxed = true)

    private val sampleTickets = listOf(
        WeighbridgeTicket(
            id = "ticket1",
            driverName = "Arief Nugraha",
            netWeight = 1000.0,
            dateTime = 8187,
            licenseNumber = "AB1234CD",
            inboundWeight = 3000.0,
            outboundWeight = 2000.0,
        ),
        WeighbridgeTicket(
            id = "ticket2",
            driverName = "Jane Smith",
            netWeight = 1500.0,
            dateTime = 8188,
            licenseNumber = "XY5678ZZ",
            inboundWeight = 4000.0,
            outboundWeight = 2500.0,
        )
    )

    @Before
    fun setup() {
        useCase = GetAllTicketsUseCase(repository)
    }

    @Test
    fun `execute should return flow of all tickets`() = runTest {
        // Given
        coEvery { repository.getAllTickets() } returns flowOf(sampleTickets)

        // When
        val result = useCase.doWork()

        // Then
        val resultList = result.toList()
        assertEquals(1, resultList.size)
        assertEquals(sampleTickets, resultList[0])
        assertEquals(2, resultList[0].size)
        assertEquals("ticket1", resultList[0][0].id)
        assertEquals("ticket2", resultList[0][1].id)
    }

    @Test(expected = IllegalStateException::class)
    fun `execute should throw when repository fails`() = runTest {
        // Given
        coEvery { repository.getAllTickets() } throws IllegalStateException()

        // When
        useCase.doWork()
    }
} 