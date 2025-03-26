package com.example.sawitproarief.presentation.screen.ticket

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.sawitproarief.R
import com.example.sawitproarief.presentation.components.EmptyState
import com.example.sawitproarief.presentation.components.FilterSection
import com.example.sawitproarief.presentation.components.WeighbridgeCard
import com.example.sawitproarief.presentation.screen.ticket.state.SortOrder
import com.example.sawitproarief.presentation.screen.ticket.state.WeighbridgeUiState
import com.example.sawitproarief.utils.Constants.FilterDefaults

@Composable
fun WeighbridgeListScreen(
    uiState: WeighbridgeUiState,
    onTicketClick: (String) -> Unit,
    onEditClick: (String) -> Unit,
    onSortOrderChange: (SortOrder) -> Unit,
    onFilterChange: (String) -> Unit,
    onDateRangeChange: (Long?, Long?) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isFiltered by remember {
        derivedStateOf {
            uiState.filterQuery.isNotBlank() ||
                    ((uiState.startDate ?: 0L) > FilterDefaults.DEFAULT_START_DATE) ||
                    ((uiState.endDate ?: 0L) > FilterDefaults.DEFAULT_END_DATE)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Ticket",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            FilterSection(
                onFilterChange = onFilterChange,
                onSortChange = onSortOrderChange,
                onDateRangeChange = onDateRangeChange,
                modifier = Modifier,
                uiState = uiState,
            )

            if (uiState.tickets.isEmpty()) {
                if (isFiltered) {
                    EmptyState(
                        title = stringResource(R.string.error_no_matching_tickets),
                        message = stringResource(R.string.error_try_adjusting_your_filters_to_find_what_you_re_looking_for),
                        isFiltered = true,
                    )
                } else {
                    EmptyState(
                        title = stringResource(R.string.error_no_weighbridge_tickets),
                        message = stringResource(R.string.error_create_your_first_weighbridge_ticket_by_clicking_the_button_below),
                        isFiltered = false,
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(uiState.tickets) { ticket ->
                        WeighbridgeCard(
                            ticket = ticket,
                            onTicketClick = onTicketClick,
                            onEditClick = onEditClick,
                        )
                    }
                }
            }

            uiState.error?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}