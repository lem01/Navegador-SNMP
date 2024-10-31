package com.example.nav_snmp.utils

class CommonOids {

    object SYSTEM {
        const val SYS_DESCR = "1.3.6.1.2.1.1.1"
        const val SYS_OBJECT_ID = "1.3.6.1.2.1.1.2"
        const val SYS_UP_TIME = "1.3.6.1.2.1.1.3"
        const val SYS_CONTACT = "1.3.6.1.2.1.1.4"
        const val SYS_NAME = "1.3.6.1.2.1.1.5"
        const val SYS_LOCATION = "1.3.6.1.2.1.1.6"
        const val SYS_SERVICES = "1.3.6.1.2.1.1.7"
    }

    object INTERFACES {
        const val IF_NUMBER = "1.3.6.1.2.1.2.1"
        const val IF_TABLE = "1.3.6.1.2.1.2.2"
        const val IF_INDEX = "1.3.6.1.2.1.2.2.1"
        const val IF_DESCR = "1.3.6.1.2.1.2.2.2"
        const val IF_TYPE = "1.3.6.1.2.1.2.2.3"
        const val IF_MTU = "1.3.6.1.2.1.2.2.4"
        const val IF_SPEED = "1.3.6.1.2.1.2.2.5"
        const val IF_PHYS_ADDRESS = "1.3.6.1.2.1.2.2.6"
        const val IF_ADMIN_STATUS = "1.3.6.1.2.1.2.2.7"
        const val IF_OPER_STATUS = "1.3.6.1.2.1.2.2.8"
        const val IF_LAST_CHANGE = "1.3.6.1.2.1.2.2.9"
    }

    object NETWORK_ADDRESSES {
        const val IF_TABLE = "1.3.6.1.2.1.3.1"
        const val IF_XTABLE = "1.3.6.1.2.1.3.2"
        const val AT_TABLE = "1.3.6.1.2.1.3.3"
        const val IP_NET_TO_MEDIA_TABBLE = "1.3.6.1.2.1.3.4"
        const val IP_ROUTING_TABLE = "1.3.6.1.2.1.3.5"
        const val TCP_CONN_TABLE = "1.3.6.1.2.1.3.6"
        const val UDP_TABLE = "1.3.6.1.2.1.3.7"
    }

    object IP {
        const val IP_FORWARDING = "1.3.6.1.2.1.4.1"
        const val IP_DEFAULT_TTL = "1.3.6.1.2.1.4.2"
        const val IP_IN_RECEIVES = "1.3.6.1.2.1.4.3"
    }

    object IP_ROUTE_TABLE {
        const val VALUE_DESTINATION_NETWORK = "1.3.6.1.2.1.4.21.1.1"
        const val VALUE_MASK_DESTINATION_NETWORK = "1.3.6.1.2.1.4.21.1.2"
        const val NEXT_HOP_ADDRESS = "1.3.6.1.2.1.4.21.1.7"
        const val TYPE_ROUTING = "1.3.6.1.2.1.4.21.1.8"
        const val NUMERIC_VALUE_OF_ROUTE = "1.3.6.1.2.1.4.21.1.11"
        const val EXPIRATION_TIME_ROUTE = "1.3.6.1.2.1.4.21.1.13"
        const val TYPE_PROTOCOL_ROUTE = "1.3.6.1.2.1.4.21.1.14"
        const val LAST_MODIFICATION_TIME_ROUTING_TABLE_ENTRY = "1.3.6.1.2.1.4.21.1.15"
        const val STARTING_TIME_ROUTING_TABLE_ENTRY = "1.3.6.1.2.1.4.21.1.18"
    }

    private val additionalOids = mutableMapOf<String, String>()

    fun getOid(name: String): String? {
        return when (name) {
            "sysDescr" -> SYSTEM.SYS_DESCR
            "sysObjectID" -> SYSTEM.SYS_OBJECT_ID
            "sysUpTime" -> SYSTEM.SYS_UP_TIME
            "sysContact" -> SYSTEM.SYS_CONTACT
            "sysName" -> SYSTEM.SYS_NAME
            "sysLocation" -> SYSTEM.SYS_LOCATION
            "sysServices" -> SYSTEM.SYS_SERVICES
            else -> additionalOids[name]
        }
    }


    fun addOid(name: String, oid: String) {
        additionalOids[name] = oid
    }

    fun getAllOids(): Map<String, String> {
        val allOids = mapOf(
            "sysDescr" to SYSTEM.SYS_DESCR,
            "sysObjectID" to SYSTEM.SYS_OBJECT_ID,
            "sysUpTime" to SYSTEM.SYS_UP_TIME,
            "sysContact" to SYSTEM.SYS_CONTACT,
            "sysName" to SYSTEM.SYS_NAME,
            "sysLocation" to SYSTEM.SYS_LOCATION,
            "sysServices" to SYSTEM.SYS_SERVICES
        )
        return allOids + additionalOids // Combina constantes y adicionales
    }
}
