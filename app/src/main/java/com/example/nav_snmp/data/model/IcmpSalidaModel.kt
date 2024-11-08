package com.example.nav_snmp.data.model

data class IcmpSalidaModel(
    val numeroDePaquetesEnviados: String,
    val numeroDePaquetesConError: String,
    val mensajeConDestinoInalcanzable: String,
    val numeroDePaquetesConTiempoExcedido: String,
    val numeroDePaquetesDeProblemasDeParametros: String,
    val controlDeFlujo: String,
    val numeroDePaquetesDeRedireccion: String,
    val numeroDePaqueteEco: String,
    val numeroDePaquetesMarcaTiempo: String,
)
