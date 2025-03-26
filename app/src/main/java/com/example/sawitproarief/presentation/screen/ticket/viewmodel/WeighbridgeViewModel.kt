package com.example.sawitproarief.presentation.screen.ticket.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sawitproarief.domain.model.WeighbridgeTicket
import com.example.sawitproarief.domain.usecase.GetAllTicketsUseCase
import com.example.sawitproarief.presentation.screen.ticket.state.SortOrder
import com.example.sawitproarief.presentation.screen.ticket.state.WeighbridgeUiState
import com.example.sawitproarief.utils.Constants.FilterDefaults
import com.example.sawitproarief.utils.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class WeighbridgeViewModel @Inject constructor(
    private val getAllTicketsUseCase: GetAllTicketsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(WeighbridgeUiState())
    
    private val _startDate = MutableStateFlow<Long?>(null)
    private val _endDate = MutableStateFlow<Long?>(null)
    private val _sortOrder = MutableStateFlow(SortOrder.DATE_DESC)
    private val _filterQuery = MutableStateFlow(FilterDefaults.EMPTY_QUERY)
    private val _refreshTrigger = MutableStateFlow(0)
    
    private val ticketsFlow = _refreshTrigger.flatMapLatest {
        try {
            getAllTicketsUseCase.doWork()
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(error = e.message)
            flowOf(emptyList())
        }
    }
    
    val uiState: StateFlow<WeighbridgeUiState> = combine(
        ticketsFlow,
        _sortOrder,
        _filterQuery,
        _startDate,
        _endDate,
        _uiState,
    ) { tickets, sortOrder, query, startDate, endDate, currentState ->
        var filteredTickets = filterTickets(tickets, query)
        
        if (startDate != null && endDate != null) {
            filteredTickets = filteredTickets.filter {
                it.dateTime in startDate..endDate
            }
        }
        
        val sortedTickets = sortTickets(filteredTickets, sortOrder)
        
        currentState.copy(
            tickets = sortedTickets,
            sortOrder = sortOrder,
            filterQuery = query,
            startDate = startDate,
            endDate = endDate
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = WeighbridgeUiState()
    )
    
    init {
        refreshData()
    }
    
    private fun refreshData() {
        _refreshTrigger.value += 1
    }

    private fun clearDateFilters() {
        _startDate.value = null
        _endDate.value = null
        refreshData()
    }

    fun updateDateRange(start: Long?, end: Long?) {
        if (start == 0L && end == 0L) {
            clearDateFilters()
            return
        }
        
        _startDate.value = start
        _endDate.value = end
        refreshData()
    }

    private fun filterTickets(
        tickets: List<WeighbridgeTicket>,
        query: String,
    ): List<WeighbridgeTicket> {
        if (query.isBlank()) return tickets
        return tickets.filter { ticket ->
            ticket.id.contains(query, true) ||
            ticket.licenseNumber.contains(query, true) ||
            ticket.driverName.contains(query, true) ||
            ticket.inboundWeight.toString().contains(query, true) ||
            ticket.outboundWeight.toString().contains(query, true) ||
            ticket.netWeight.toString().contains(query, true)
        }
    }

    private fun sortTickets(
        tickets: List<WeighbridgeTicket>,
        sortOrder: SortOrder
    ): List<WeighbridgeTicket> {
        return when (sortOrder) {
            SortOrder.DATE_DESC -> tickets.sortedByDescending { it.dateTime }
            SortOrder.DATE_ASC -> tickets.sortedBy { it.dateTime }
            SortOrder.DRIVER_NAME -> tickets.sortedBy { it.driverName }
            SortOrder.LICENSE_NUMBER -> tickets.sortedBy { it.licenseNumber }
        }
    }

    fun updateSortOrder(sortOrder: SortOrder) {
        _sortOrder.value = sortOrder
    }

    fun updateFilter(query: String) {
        _filterQuery.value = query
    }
}




