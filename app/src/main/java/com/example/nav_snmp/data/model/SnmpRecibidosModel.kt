package com.example.nav_snmp.data.model

data class SnmpRecibidosModel(
    val variablesSolicitudesGetSetRecibidas: String,
    val variablesSolicitudesSetRecibidas: String,
    val solicitudesGetRecibidas: String,
    val solicitudesGetNextRecibidas: String,
    val solicitudesSetRecibidas: String,
    val respuestasGetRecibidas: String,
    val mensajesTrapSnmpRecibidos: String,
    val conVersionSnmpNoSoportada: String,
    val nombreComunidadSnmpInvalido: String,
    val conUsoInapropiadoDeComunidad: String,
    val erroresDeSintaxisAsn1: String,
    val grandeParaCaberEnUnMensaje: String,
    val objetoSolicitadoNoExiste: String,
    val solicitudesDeEscrituraAObjetosDeLectura: String,
    val erroresGenericosNoEspecificados: String
)
