package com.example.nav_snmp.data.model

data class SnmpEnviadosModel(
    val totalPaquetesSnmpEnviados: String,
    val solicitudesGetEnviadas: String,
    val solicitudesGetNextEnviadas: String,
    val solicitudesSetEnviadas: String,
    val respuestasGetEnviadas: String,
    val trapsSnmpEnviados: String,

    val respuestaEsDemasiadoGrande: String,
    val objetoSolicitadoNoExiste: String,
    val invalidosEnOperacionesSet: String,
    val erroresGenericosNoEspecificados: String
)
