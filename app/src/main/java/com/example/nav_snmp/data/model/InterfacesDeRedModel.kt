package com.example.nav_snmp.data.model

data class InterfacesDeRedModel(
    val descripcion: String,
    val tipo: String,
    val velocidad: String,
    val direccionMac: String,
    val estadoOperativo: String,
    val estadoAdministrativo: String,
    val numeroDeBytesRecividos: String,
    val numeroDeBytesEnviados: String,
)
