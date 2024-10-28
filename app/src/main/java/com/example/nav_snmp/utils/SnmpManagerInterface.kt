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
    suspend fun descubrirHost(hostModel: HostModel, context: Context): List<HostModel>
    abstract fun mensajeAlert(context: Context, s: String, s1: String, successType: Int)
}