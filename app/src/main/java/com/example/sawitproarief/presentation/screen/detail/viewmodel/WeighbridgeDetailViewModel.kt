package com.example.sawitproarief.presentation.screen.detail.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sawitproarief.domain.model.WeighbridgeTicket
import com.example.sawitproarief.domain.usecase.DeleteTicketUseCase
import com.example.sawitproarief.domain.usecase.GetTicketUseCase
import com.example.sawitproarief.presentation.screen.detail.WeighbridgeDetailUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeighbridgeDetailViewModel @Inject constructor(
    private val getTicketUseCase: GetTicketUseCase,
    private val deleteTicketUseCase: DeleteTicketUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _ticket = MutableStateFlow<WeighbridgeTicket?>(null)
    private val _error = MutableStateFlow<String?>(null)

    val uiState = combine(_ticket, _error) { ticket, error ->
        WeighbridgeDetailUIState(ticket, error)
    }.stateIn(viewModelScope, SharingStarted.Lazily, WeighbridgeDetailUIState(null, null))

    init {
        savedStateHandle.get<String>("ticketId")?.let { ticketId ->
            loadTicket(ticketId)
        }
    }

    private fun loadTicket(id: String) {
        viewModelScope.launch {
            try {
                getTicketUseCase.doWork(GetTicketUseCase.Param(id))
                    .collect { result ->
                        _ticket.value = result
                        _error.value = null
                    }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deleteTicket(id: String) {
        viewModelScope.launch {
            try {
                deleteTicketUseCase.doWork(DeleteTicketUseCase.Param(id))
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}