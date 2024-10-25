package com.example.snmp.data.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

interface HostModelInterface {
    val id: Int
    val nombreHost: String
    val direccionIP: String
    val tipoDeDispositivo: String
    val versionSNMP: String
    val puertoSNMP: Int
    val comunidadSNMP: String
    val estado: Boolean
    val fecha: String?
}

@Entity(tableName = "Host")
data class HostModel(
    @PrimaryKey(autoGenerate = true) override val id: Int,
    @ColumnInfo(name = "nombreHost") override val nombreHost: String,
    @ColumnInfo(name = "direccionIP") override val direccionIP: String,
    @ColumnInfo(name = "tipoDeDispositivo") override val tipoDeDispositivo: String,
    @ColumnInfo(name = "versionSNMP") override val versionSNMP: String,
    @ColumnInfo(name = "puertoSNMP") override val puertoSNMP: Int,
    @ColumnInfo(name = "comunidadSNMP") override val comunidadSNMP: String,
    @ColumnInfo(name = "estado") override val estado: Boolean,
    @ColumnInfo(name = "fecha") override val fecha: String?
) : HostModelInterface

class HostModelClass(
    override val id: Int,
    override val nombreHost: String,
    override val direccionIP: String,
    override val tipoDeDispositivo: String,
    override val versionSNMP: String,
    override val puertoSNMP: Int,
    override val comunidadSNMP: String,
    override val estado: Boolean,
    override val fecha: String?,
    initialChecked: Boolean = false
) : HostModelInterface {
    var checked by mutableStateOf(initialChecked)
}
