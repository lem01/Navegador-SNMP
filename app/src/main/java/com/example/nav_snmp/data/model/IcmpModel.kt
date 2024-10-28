package com.example.nav_snmp.data.model

data class IcmpModel(
    val ip: String,
    val ttl: Int,
    val rtt: Long,
    val status: String,
    val type: String
)