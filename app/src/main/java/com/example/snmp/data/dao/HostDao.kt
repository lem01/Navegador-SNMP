package com.example.snmp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.snmp.data.model.HostModel
import kotlinx.coroutines.flow.Flow

@Dao
interface HostDao {
    @Query("SELECT * FROM Host")
    fun getAll(): Flow<List<HostModel>>

    @Query("SELECT * FROM Host WHERE id = :id")
    fun getById(id: Int): HostModel

    @Insert
    fun insert(host: HostModel)

    @Insert
    fun insertAll(vararg hosts: HostModel)

    @Query("DELETE FROM Host")
    fun deleteAll()

    @Query("DELETE FROM Host WHERE id = :id")
    fun deleteById(id: Int)


}