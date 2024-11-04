package com.example.nav_snmp.utils

import android.content.Context
import com.example.nav_snmp.data.model.HostModel

enum class VersionSnmp {
    V1, V2c
}

interface SnmpManagerInterface {
    fun snmpTest(hostModel: HostModel, context: Context)
    fun close()
    fun getOid()
    fun setOid()
    suspend fun get(hostModel: HostModel,oid: String,tipoOperacion: TipoOperacion, context: Context): Any
    fun getNext(hostModel: HostModel, context: Context)
    fun set(hostModel: HostModel, context: Context)
    fun walk(hostModel: HostModel, context: Context)

    abstract fun mensajeAlert(context: Context, s: String, s1: String, successType: Int)
}