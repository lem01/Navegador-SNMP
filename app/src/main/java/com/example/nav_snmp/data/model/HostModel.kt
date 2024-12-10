package com.example.nav_snmp.data.model

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
    override var id: Int,
    override var nombreHost: String,
    override var direccionIP: String,
    override var tipoDeDispositivo: String,
    override var versionSNMP: String,
    override var puertoSNMP: Int,
    override var comunidadSNMP: String,
    override var estado: Boolean,
    override var fecha: String?,
    initialChecked: Boolean = false
) : HostModelInterface {
    var checked by mutableStateOf(initialChecked)

    fun copy(
        id: Int = this.id,
        nombreHost: String = this.nombreHost,
        direccionIP: String = this.direccionIP,
        tipoDeDispositivo: String = this.tipoDeDispositivo,
        versionSNMP: String = this.versionSNMP,
        puertoSNMP: Int = this.puertoSNMP,
        comunidadSNMP: String = this.comunidadSNMP,
        estado: Boolean = this.estado,
        fecha: String? = this.fecha,
        checked: Boolean = this.checked
    ) = HostModelClass(
        id, nombreHost, direccionIP, tipoDeDispositivo, versionSNMP, puertoSNMP, comunidadSNMP, estado, fecha, checked
    )
}
