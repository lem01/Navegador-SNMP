package com.example.snmp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.snmp.data.dao.HostDao
import com.example.snmp.data.model.HostModel

@Database(entities = [HostModel::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun hostDao(): HostDao
}