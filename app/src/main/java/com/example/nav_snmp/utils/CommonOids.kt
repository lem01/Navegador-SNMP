package com.example.nav_snmp.utils

class CommonOids {

    companion object {
        const val SYS_DESCR = "1.3.6.1.2.1.1.1"
        const val SYS_OBJECT_ID = "1.3.6.1.2.1.1.2"
        const val SYS_UP_TIME = "1.3.6.1.2.1.1.3"
        const val SYS_CONTACT = "1.3.6.1.2.1.1.4"
        const val SYS_NAME = "1.3.6.1.2.1.1.5"
        const val SYS_LOCATION = "1.3.6.1.2.1.1.6"
        const val SYS_SERVICES = "1.3.6.1.2.1.1.7"
    }

    private val additionalOids = mutableMapOf<String, String>()

    fun getOid(name: String): String? {
        return when (name) {
            "sysDescr" -> SYS_DESCR
            "sysObjectID" -> SYS_OBJECT_ID
            "sysUpTime" -> SYS_UP_TIME
            "sysContact" -> SYS_CONTACT
            "sysName" -> SYS_NAME
            "sysLocation" -> SYS_LOCATION
            "sysServices" -> SYS_SERVICES
            else -> additionalOids[name]
        }
    }


    fun addOid(name: String, oid: String) {
        additionalOids[name] = oid
    }

    fun getAllOids(): Map<String, String> {
        val allOids = mapOf(
            "sysDescr" to SYS_DESCR,
            "sysObjectID" to SYS_OBJECT_ID,
            "sysUpTime" to SYS_UP_TIME,
            "sysContact" to SYS_CONTACT,
            "sysName" to SYS_NAME,
            "sysLocation" to SYS_LOCATION,
            "sysServices" to SYS_SERVICES
        )
        return allOids + additionalOids // Combina constantes y adicionales
    }
}
