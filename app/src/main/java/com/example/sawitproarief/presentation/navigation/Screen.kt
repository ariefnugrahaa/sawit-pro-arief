package com.example.sawitproarief.presentation.navigation

sealed class Screen(val route: String) {
    object List : Screen("list")
    object Detail : Screen("detail?ticketId={ticketId}") {
        const val TICKET_ID = "ticketId"
        fun createRoute(ticketId: String) = "detail?ticketId=$ticketId"
    }
    object Form : Screen("form?ticketId={ticketId}") {
        const val TICKET_ID = "ticketId"
        fun createRoute(ticketId: String = "") = "form?ticketId=$ticketId"
    }
}