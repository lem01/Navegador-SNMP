package com.example.snmp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//id no null autoincrement
//utf8_general_ci
//utf8_spanish_ci
@Entity(tableName = "Host")
data class HostModel(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "nombreHost") val nombreHost: String,
    @ColumnInfo(name = "direccionIP") val direccionIP: String,
    @ColumnInfo(name = "tipoDeDispositivo") val tipoDeDispositivo: String,
    @ColumnInfo(name = "versionSNMP") val versionSNMP: String,
    @ColumnInfo(name = "puertoSNMP") val puertoSNMP: Int,
    @ColumnInfo(name = "comunidadSNMP") val comunidadSNMP: String,
    @ColumnInfo(name = "estado") val estado: Boolean,
    @ColumnInfo(name = "fecha") val fecha: String?,
)
