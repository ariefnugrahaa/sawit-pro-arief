package com.example.sawitproarief.presentation.screen.form.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sawitproarief.domain.model.WeighbridgeTicket
import com.example.sawitproarief.domain.usecase.CreateTicketUseCase
import com.example.sawitproarief.domain.usecase.GetTicketUseCase
import com.example.sawitproarief.domain.usecase.UpdateTicketUseCase
import com.example.sawitproarief.presentation.screen.form.state.WeighbridgeFormState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeighbridgeFormViewModel @Inject constructor(
    private val createTicketUseCase: CreateTicketUseCase,
    private val updateTicketUseCase: UpdateTicketUseCase,
    private val getTicketUseCase: GetTicketUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeighbridgeFormState())
    val uiState = _uiState.asStateFlow()

    fun createTicket(ticket: WeighbridgeTicket) {
        viewModelScope.launch {
            try {
                createTicketUseCase.doWork(CreateTicketUseCase.Param(ticket))
                _uiState.update { state -> state.copy(error = null) }
            } catch (e: Exception) {
                _uiState.update { state -> state.copy(error = e.message) }
            }
        }
    }

    fun updateTicket(ticket: WeighbridgeTicket) {
        viewModelScope.launch {
            try {
                updateTicketUseCase.doWork(UpdateTicketUseCase.Param(ticket))
                _uiState.update { state -> state.copy(error = null) }
            } catch (e: Exception) {
                _uiState.update { state -> state.copy(error = e.message) }
            }
        }
    }

    fun loadTicket(ticketId: String) {
        if (ticketId.isEmpty()) {
            _uiState.update { state -> state.copy(currentTicket = null) }
            return
        }

        viewModelScope.launch {
            try {
                getTicketUseCase.doWork(GetTicketUseCase.Param(ticketId))
                    .collect { ticket ->
                        _uiState.update { state -> state.copy(currentTicket = ticket) }
                    }
                _uiState.update { state -> state.copy(error = null) }
            } catch (e: Exception) {
                _uiState.update { state -> state.copy(error = e.message) }
            }
        }
    }
}