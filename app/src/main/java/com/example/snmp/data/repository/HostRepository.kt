package com.example.snmp.data.repository

import android.content.Context
import androidx.room.Room
import com.example.snmp.data.database.AppDatabase
import com.example.snmp.data.model.HostModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HostRepository(context: Context) {
    private val db: AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java, "mib_database"
    )
        .fallbackToDestructiveMigration()
        .fallbackToDestructiveMigrationOnDowngrade()
        .build()
    private val hostDao = db.hostDao()

    // Método para guardar el host en la base de datos
    suspend fun saveHost(host: HostModel) {
        withContext(Dispatchers.IO) {
            hostDao.insert(host)
        }
    }

    suspend fun getAllHosts(): List<HostModel> {
        return withContext(Dispatchers.IO) {
            hostDao.getAll()
        }
    }

    ///delete item
    suspend fun deleteHost(id: Int) {
        withContext(Dispatchers.IO) {
            hostDao.deleteById(id)
        }
    }

    suspend fun getHostById(idHost: Int): HostModel {
        return withContext(Dispatchers.IO) {
            hostDao.getById(idHost)
        }
    }

    suspend fun updateHost(host: HostModel) {
        withContext(Dispatchers.IO) {
            hostDao.update(host)
        }

    }
}
