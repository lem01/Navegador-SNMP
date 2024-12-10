package com.example.nav_snmp.data.model

data class DatosIpModel(
    val estadoReenvioPaquetes: String,
    val ttlPredeterminado: String,
    val totalDatagramasIpRecibidos: String,
    val erroresCabecera: String,
    val erroresDireccion: String,
    val datagramasIpReenviados: String,
    val datagranasConProtcolosDesconocidos: String,
    val descartadosSinError: String,
    val entregadosCorrectamente: String,
    val generadosParaEnvio: String,
    val descartadosAlEnviar: String,
    val sinRutaDisponible: String,
    val tiempoDesEsperaReensamblajeDeFragmentos: String,
    val solicitudesReensambleajeDeFragmentos: String,
    val reensamblajeExitosos: String,
    val fallosEnElReensamblajeDatagramasIP: String,
    val fragmentacionExitosaDatagramasIp: String,
    val fallosEnlaFragmentacionDeDatagramasIp: String,
    val fragmentosGeneradosPorFragmentacion: String,
    val entradasEnrutamientoDescartadas: String,
)
