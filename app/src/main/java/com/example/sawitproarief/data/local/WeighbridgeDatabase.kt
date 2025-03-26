package com.example.sawitproarief.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sawitproarief.data.local.entity.WeighbridgeTicketEntity

@Database(
    entities = [WeighbridgeTicketEntity::class],
    version = 1
)
abstract class WeighbridgeDatabase : RoomDatabase() {
    abstract fun weighbridgeDao(): WeighbridgeDao
}