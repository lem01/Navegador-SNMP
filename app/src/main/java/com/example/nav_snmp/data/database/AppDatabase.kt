package com.example.nav_snmp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.nav_snmp.data.dao.HostDao
import com.example.nav_snmp.data.model.HostModel

@Database(entities = [HostModel::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun hostDao(): HostDao
}