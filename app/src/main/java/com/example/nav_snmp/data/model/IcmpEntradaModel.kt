package com.example.nav_snmp.data.model

data class IcmpEntradaModel(
    val numeroDePaquetesRecibidos: String,
    val numeroDePaquetesConError: String,
    val mensejeConDestinoInalcanzable: String,
    val numeroDePaquetesConTiempoExcedido: String,
    val numeroDePaquetesDeProblemasDeParametros: String,
    val controlDeFlujo: String,
    val numeroDePaquetesDeRedireccion: String,
    val numeroDePaqueteEco: String,
    val numeroDePaquetesMarcaTiempo: String,
)
