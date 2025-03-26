package com.example.sawitproarief.presentation.components

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.sawitproarief.R
import com.example.sawitproarief.presentation.screen.ticket.state.SortOrder
import com.example.sawitproarief.presentation.screen.ticket.state.WeighbridgeUiState
import com.example.sawitproarief.utils.Constants.DATE_FORMAT_PATTERN
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun FilterSection(
    onFilterChange: (String) -> Unit,
    onSortChange: (SortOrder) -> Unit,
    onDateRangeChange: (Long?, Long?) -> Unit,
    modifier: Modifier = Modifier,
    uiState: WeighbridgeUiState,
) {
    var filterQuery by remember { mutableStateOf(uiState.filterQuery) }
    var sortExpanded by remember { mutableStateOf(false) }
    var filterExpanded by remember { mutableStateOf(false) }

    var startDate by remember { mutableStateOf(uiState.startDate) }
    var endDate by remember { mutableStateOf(uiState.endDate) }

    val dateFormatter = remember { SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.getDefault()) }
    val context = LocalContext.current

    LaunchedEffect(uiState.filterQuery) {
        if (filterQuery != uiState.filterQuery) {
            filterQuery = uiState.filterQuery
        }
    }

    LaunchedEffect(uiState.startDate, uiState.endDate) {
        startDate = uiState.startDate
        endDate = uiState.endDate
    }

    val startDatePicker = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val calendar = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth, 0, 0, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                startDate = calendar.timeInMillis
                if (endDate != null) {
                    onDateRangeChange(calendar.timeInMillis, endDate)
                } else {
                    val endCalendar = Calendar.getInstance()
                    endCalendar.set(Calendar.HOUR_OF_DAY, 23)
                    endCalendar.set(Calendar.MINUTE, 59)
                    endCalendar.set(Calendar.SECOND, 59)
                    endCalendar.set(Calendar.MILLISECOND, 999)
                    endDate = endCalendar.timeInMillis
                    onDateRangeChange(calendar.timeInMillis, endCalendar.timeInMillis)
                }
            },
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )
    }

    val endDatePicker = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val calendar = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth, 23, 59, 59)
                    set(Calendar.MILLISECOND, 999)
                }
                endDate = calendar.timeInMillis
                if (startDate != null) {
                    onDateRangeChange(startDate, calendar.timeInMillis)
                } else {
                    onDateRangeChange(null, calendar.timeInMillis)
                }
            },
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )
    }

    if (startDate != null) {
        endDatePicker.datePicker.minDate = startDate!!
    }

    if (endDate != null) {
        startDatePicker.datePicker.maxDate = endDate!!
    }

    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = filterQuery,
            onValueChange = {
                filterQuery = it
                onFilterChange(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text(stringResource(R.string.search_placeholder)) },
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { filterExpanded = true }) {
                    Icon(Icons.Default.DateRange, "Filter")
                }

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = buildString {
                        append(
                            if (startDate != null) {
                                dateFormatter.format(Date(startDate!!))
                            } else {
                                stringResource(R.string.text_start_date)
                            }
                        )
                        append(" - ")
                        append(
                            if (endDate != null) {
                                dateFormatter.format(Date(endDate!!))
                            } else {
                                stringResource(R.string.text_end_date)
                            }
                        )
                    },
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Column {
                IconButton(onClick = { sortExpanded = true }) {
                    Icon(Icons.Default.List, "Filter")
                }

                DropdownMenu(
                    expanded = sortExpanded,
                    onDismissRequest = { sortExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.filter_date_newest)) },
                        onClick = {
                            onSortChange(SortOrder.DATE_DESC)
                            sortExpanded = false
                        },
                        trailingIcon = {
                            if (uiState.sortOrder == SortOrder.DATE_DESC) {
                                Icon(Icons.Default.Check, contentDescription = "Selected")
                            }
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.filter_date_oldest)) },
                        onClick = {
                            onSortChange(SortOrder.DATE_ASC)
                            sortExpanded = false
                        },
                        trailingIcon = {
                            if (uiState.sortOrder == SortOrder.DATE_ASC) {
                                Icon(Icons.Default.Check, contentDescription = "Selected")
                            }
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.filter_driver_name)) },
                        onClick = {
                            onSortChange(SortOrder.DRIVER_NAME)
                            sortExpanded = false
                        },
                        trailingIcon = {
                            if (uiState.sortOrder == SortOrder.DRIVER_NAME) {
                                Icon(Icons.Default.Check, contentDescription = "Selected")
                            }
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.filter_license_number)) },
                        onClick = {
                            onSortChange(SortOrder.LICENSE_NUMBER)
                            sortExpanded = false
                        },
                        trailingIcon = {
                            if (uiState.sortOrder == SortOrder.LICENSE_NUMBER) {
                                Icon(Icons.Default.Check, contentDescription = "Selected")
                            }
                        }
                    )
                }
            }
        }

        DropdownMenu(
            expanded = filterExpanded,
            onDismissRequest = { filterExpanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.text_set_start_date)) },
                onClick = {
                    startDatePicker.show()
                    filterExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.text_set_end_date)) },
                onClick = {
                    endDatePicker.show()
                    filterExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.text_clear_date_filter)) },
                onClick = {
                    startDate = null
                    endDate = null
                    onDateRangeChange(null, null)
                    filterExpanded = false
                }
            )
        }
    }
}