package com.example.sawitproarief.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sawitproarief.presentation.screen.form.WeighbridgeFormScreen
import com.example.sawitproarief.presentation.screen.ticket.WeighbridgeListScreen
import com.example.sawitproarief.presentation.screen.detail.WeighbridgeDetailScreen
import com.example.sawitproarief.presentation.screen.detail.viewmodel.WeighbridgeDetailViewModel
import com.example.sawitproarief.presentation.screen.ticket.viewmodel.WeighbridgeViewModel

@Composable
fun WeighbridgeNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.List.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.List.route) {
            val viewModel = hiltViewModel<WeighbridgeViewModel>()
            val uiState by viewModel.uiState.collectAsState()
            
            WeighbridgeListScreen(
                uiState = uiState,
                onTicketClick = { ticketId ->
                    navController.navigate(Screen.Detail.createRoute(ticketId))
                },
                onSortOrderChange = viewModel::updateSortOrder,
                onFilterChange = viewModel::updateFilter,
                onAddClick = {
                    navController.navigate(Screen.Form.createRoute())
                },
                onDateRangeChange = viewModel::updateDateRange,
                onEditClick = { ticketId ->
                    navController.navigate(Screen.Form.createRoute(ticketId))
                },
            )
        }
        
        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument(Screen.Detail.TICKET_ID) { 
                    type = NavType.StringType
                    nullable = false
                    defaultValue = ""
                }
            )
        ) {
            val viewModel = hiltViewModel<WeighbridgeDetailViewModel>()
            val uiState by viewModel.uiState.collectAsState()
            uiState.ticket?.let { ticketData ->
                WeighbridgeDetailScreen(
                    ticket = ticketData,
                    onNavigateBack = { navController.popBackStack() },
                    onEditClick = { ticketId ->
                        navController.navigate(Screen.Form.createRoute(ticketId))
                    },
                    onDeleteClick = { id ->
                        viewModel.deleteTicket(id)
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(
            route = Screen.Form.route,
            arguments = listOf(
                navArgument(Screen.Form.TICKET_ID) {
                    type = NavType.StringType
                    nullable = false
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val ticketId = backStackEntry.arguments?.getString(Screen.Form.TICKET_ID) ?: ""
            
            WeighbridgeFormScreen(
                ticketId = ticketId,
                onSubmit = {
                    navController.popBackStack()
                }
            )
        }
    }
}