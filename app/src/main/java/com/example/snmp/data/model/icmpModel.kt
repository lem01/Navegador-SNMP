package com.example.snmp.data.model

data class icmpModel(
    val ip: String,
    val ttl: Int,
    val rtt: Long,
    val status: String,
    val type: String
)