package com.example.nav_snmp.data.model

data class TablaDeConexionesModel(
    val estadoActual: String,
    val direccionIpLocal: String,
    val puertoLocal: String,
    val direccionIpRemota: String,
    val puertoRemoto: String,
)
