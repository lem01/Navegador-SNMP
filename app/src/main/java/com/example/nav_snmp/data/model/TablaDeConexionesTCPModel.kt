package com.example.nav_snmp.data.model

data class TablaDeConexionesTCPModel(
    val estadoActual: String,
    val direccionIpLocal: String,
    val puertoLocal: String,
    val direccionIpRemota: String,
    val puertoRemoto: String,
)
