package com.example.nav_snmp.data.model

data class InformacionSistemaModel(
    val tiempoDeFuncionamiento: String,
    val fecha: String,
    val numeroDeUsuarios: String,
    val numeroDeProcesos: String,
    val maximoNumeroDeProcesos: String,
    var nombre: String,
    val descripcion: String,
    val localizacion: String,
    val contacto: String,
)
