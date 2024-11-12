package com.example.nav_snmp.utils

import android.content.Context
import com.example.nav_snmp.data.model.HostModel

enum class VersionSnmp {
    V1, V2c
}

enum class TipoDeBusqueda {
    SNMP, ICMP
}

interface SnmpManagerInterface {
    fun snmpTest(hostModel: HostModel, context: Context)
    fun close()
    suspend fun getOid(vararg args: Any): String

    suspend fun operacionSnmp(vararg args: Any): Any
    suspend fun get(
        hostModel: HostModel,
        oid: String,
        tipoOperacion: TipoOperacion,
        context: Context,
        isShowPgrogress: Boolean = true,
    ): Any

    suspend fun getNext(vararg args: Any): Any
    suspend fun set(vararg args: Any): Any
    suspend fun walk(
        hostModel: HostModel,
        oid: String,
        context: Context,
        isShowProgress: Boolean
    ): List<String>

    abstract fun mensajeAlert(context: Context, s: String, s1: String, successType: Int)

}
