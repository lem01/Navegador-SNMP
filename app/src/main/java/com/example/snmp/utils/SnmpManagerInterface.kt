package com.example.snmp.utils

import android.content.Context
import com.example.snmp.data.model.HostModel

interface SnmpManagerInterface {
    fun snmpTest(hostModel: HostModel, context: Context)
    fun close()
    fun getOid()
    fun setOid()
    abstract fun mensajeAlert(context: Context, s: String, s1: String, successType: Int)
}