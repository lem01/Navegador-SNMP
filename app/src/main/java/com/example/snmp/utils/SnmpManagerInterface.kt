package com.example.snmp.utils

import android.content.Context
import com.example.snmp.data.model.HostModel

enum class VersionSnmp {
    V1, V2c
}

interface SnmpManagerInterface {
    fun snmpTest(hostModel: HostModel, context: Context)
    fun close()
    fun getOid()
    fun setOid()
    fun descubrirHost(hostModel: HostModel, context: Context): List<HostModel>
    abstract fun mensajeAlert(context: Context, s: String, s1: String, successType: Int)
}