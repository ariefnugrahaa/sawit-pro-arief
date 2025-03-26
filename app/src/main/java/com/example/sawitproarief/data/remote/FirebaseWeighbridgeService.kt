package com.example.sawitproarief.data.remote

import android.util.Log
import com.example.sawitproarief.data.local.WeighbridgeDao
import com.example.sawitproarief.domain.model.WeighbridgeTicket
import com.example.sawitproarief.domain.model.toEntity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseWeighbridgeService @Inject constructor(
    private val localDataSource: WeighbridgeDao,
    private val database: FirebaseDatabase
) {
    private val ticketsRef = database.getReference(DATABASE_REFERENCE)

    init {
        observeFirebaseChanges()
    }

    private fun observeFirebaseChanges() {
        ticketsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                CoroutineScope(Dispatchers.IO).launch {
                    val localIds = localDataSource.getAllTicketIds()
                    val firebaseTickets = snapshot.children.mapNotNull {
                        it.getValue(WeighbridgeTicket::class.java) 
                    }
                    val firebaseIds = firebaseTickets.map { it.id }
                    
                    val deletedIds = localIds.filter { it !in firebaseIds }
                    deletedIds.forEach { id ->
                        localDataSource.deleteTicketById(id)
                    }
                    
                    localDataSource.upsertTickets(firebaseTickets.map { it.toEntity() })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseService", "Error: ${error.message}")
            }
        })
    }

    suspend fun createTicket(ticket: WeighbridgeTicket) {
        ticketsRef.child(ticket.id).setValue(ticket).await()
    }

    suspend fun updateTicket(ticket: WeighbridgeTicket) {
        ticketsRef.child(ticket.id).setValue(ticket).await()
    }

    suspend fun deleteTicket(id: String) {
        ticketsRef.child(id).removeValue().await()
    }

    companion object {
        private const val DATABASE_REFERENCE = "tickets"
    }
}