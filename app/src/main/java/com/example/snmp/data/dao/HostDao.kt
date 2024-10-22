package com.example.snmp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.snmp.data.model.HostModel


@Dao
interface HostDao {
    @Query("SELECT * FROM Host")
    fun getAll(): List<HostModel>

    @Query("SELECT * FROM Host WHERE id = :id")
    fun getById(id: Int): HostModel

    @Insert
    fun insert(host: HostModel)

    @Insert
    fun insertAll(vararg hosts: HostModel)

    //no actualzar la fecha
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(host: HostModel)


    @Query("DELETE FROM Host")
    fun deleteAll()

    @Query("DELETE FROM Host WHERE id = :id")
    fun deleteById(id: Int)


}