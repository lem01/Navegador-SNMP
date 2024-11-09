package com.example.nav_snmp.data.model

data class DatosTcpModel(
    val tiempoMinimoDeReTransmision: String,
    val tiempoMaximoDeReTransmision: String,
    val maximoDeConexiones: String,
    val conexionesActivasIniciadas: String,
    val conexionesPasivasEstablecidas: String,
    val intentosDeConexionesFallidos: String,
    val reinciosDeConexiones: String,
    val conexionesEstablecidasActuales: String,
    val segmentosRecividios: String,
    val segmentosEnviados: String,
    val segmentosReTransmitidos: String,
)
