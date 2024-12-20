package com.example.nav_snmp.utils

class CommonOids {

    object SYSTEM {
        const val SYS_DESCR = "1.3.6.1.2.1.1.1.0"
        const val SYS_OBJECT_ID = "1.3.6.1.2.1.1.2.0"
        const val SYS_UP_TIME = "1.3.6.1.2.1.1.3.0"
        const val SYS_CONTACT = "1.3.6.1.2.1.1.4.0"
        const val SYS_NAME = "1.3.6.1.2.1.1.5.0"
        const val SYS_LOCATION = "1.3.6.1.2.1.1.6.0"
        const val SYS_SERVICES = "1.3.6.1.2.1.1.7.0"
    }

    object ICMP {
        const val ICMP_IN_MSGS = ".1.3.6.1.2.1.5.1.0"
        const val ICMP_IN_ERRORS = ".1.3.6.1.2.1.5.2.0"
        const val ICMP_IN_DEST_UNREACH = ".1.3.6.1.2.1.5.3.0"
        const val ICMP_IN_TIME_EXCDS = ".1.3.6.1.2.1.5.4.0"
        const val ICMP_IN_PROBS = ".1.3.6.1.2.1.5.5.0"
        const val ICMP_IN_SRC_QENCHS = ".1.3.6.1.2.1.5.6.0"
        const val ICMP_IN_REDIRECTS = ".1.3.6.1.2.1.5.7.0"
        const val ICMP_IN_ECHOS = ".1.3.6.1.2.1.5.8.0"
        const val ICMP_IN_ECHO_REPS = ".1.3.6.1.2.1.5.9.0"
        const val ICMP_IN_TIMESTAMPS = ".1.3.6.1.2.1.5.10.0"
        const val ICMP_IN_TIMESTAMP_REPS = ".1.3.6.1.2.1.5.11.0"
        const val ICMP_IN_ADDR_MASKS = ".1.3.6.1.2.1.5.12.0"
        const val ICMP_IN_ADDR_MASK_REPS = ".1.3.6.1.2.1.5.13.0"

        const val ICMP_OUT_MSGS = ".1.3.6.1.2.1.5.14.0"
        const val ICMP_OUT_ERRORS = ".1.3.6.1.2.1.5.15.0"
        const val ICMP_OUT_DEST_UNREACH = ".1.3.6.1.2.1.5.16.0"
        const val ICMP_OUT_TIME_EXCDS = ".1.3.6.1.2.1.5.17.0"
        const val ICMP_OUT_PROBS = ".1.3.6.1.2.1.5.18.0"
        const val ICMP_OUT_SRC_QENCHS = ".1.3.6.1.2.1.5.19.0"
        const val ICMP_OUT_REDIRECTS = ".1.3.6.1.2.1.5.20.0"
        const val ICMP_OUT_ECHOS = ".1.3.6.1.2.1.5.21.0"
        const val ICMP_OUT_ECHO_REPS = ".1.3.6.1.2.1.5.22.0"
        const val ICMP_OUT_TIMESTAMPS = ".1.3.6.1.2.1.5.23.0"
        const val ICMP_OUT_TIMESTAMP_REPS = ".1.3.6.1.2.1.5.24.0"
        const val ICMP_OUT_ADDR_MASKS = ".1.3.6.1.2.1.5.25.0"
        const val ICMP_OUT_ADDR_MASK_REPS = ".1.3.6.1.2.1.5.26.0"
    }

    object HOST {
        const val HR_SYSTEM_UPTIME = "1.3.6.1.2.1.25.1.1.0"
        const val HR_SYSTEM_DATE = "1.3.6.1.2.1.25.1.2.0"
        const val HR_SYSTEM_NUM_USERS = "1.3.6.1.2.1.25.1.5.0"
        const val HR_SYSTEM_PROCESSES = "1.3.6.1.2.1.25.1.6.0"
        const val HR_SYSTEM_MAX_PROCESSES = "1.3.6.1.2.1.25.1.7.0"

        object HRSTORAGE {
            const val HR_STORAGE_TYPE = ".1.3.6.1.2.1.25.2.3.1.2"
            const val HR_STORAGE_DESCR = ".1.3.6.1.2.1.25.2.3.1.3"
            const val HR_STORAGE_ALLOCATION_UNITS = ".1.3.6.1.2.1.25.2.3.1.4"
            const val HR_STORAGE_SIZE = ".1.3.6.1.2.1.25.2.3.1.5"
            const val HR_STORAGE_USED = ".1.3.6.1.2.1.25.2.3.1.6"
        }

        object HR_SWRUN {
            const val HR_SWRUN_NAME = ".1.3.6.1.2.1.25.4.2.1.2"
            const val HR_SWRUN_ID = ".1.3.6.1.2.1.25.4.2.1.3"
            const val HR_SWRUN_PATH = ".1.3.6.1.2.1.25.4.2.1.4"
            const val HR_SWRUN_PARAMETERS = ".1.3.6.1.2.1.25.4.2.1.5"
            const val HR_SWRUN_TYPE = ".1.3.6.1.2.1.25.4.2.1.6"
            const val HR_SWRUN_STATUS = ".1.3.6.1.2.1.25.4.2.1.7"
        }
    }

    object TCP {
        const val TCP_RTO_ALGORITHM = ".1.3.6.1.2.1.6.1.0"
        const val TCP_RTO_MIN = ".1.3.6.1.2.1.6.2.0"
        const val TCP_RTO_MAX = ".1.3.6.1.2.1.6.3.0"
        const val TCP_MAX_CONN = ".1.3.6.1.2.1.6.4.0"
        const val TCP_ACTIVE_OPENS = ".1.3.6.1.2.1.6.5.0"
        const val TCP_PASSIVE_OPENS = ".1.3.6.1.2.1.6.6.0"
        const val TCP_ATTEMPT_FAILS = ".1.3.6.1.2.1.6.7.0"
        const val TCP_ESTAB_RESETS = ".1.3.6.1.2.1.6.8.0"
        const val TCP_CURR_ESTAB = ".1.3.6.1.2.1.6.9.0"
        const val TCP_IN_SEGS = ".1.3.6.1.2.1.6.10.0"
        const val TCP_OUT_SEGS = ".1.3.6.1.2.1.6.11.0"
        const val TCP_RETRANS_SEGS = ".1.3.6.1.2.1.6.12.0"

        // Tabla de conexiones TCP (TCP Connection Table)
        object TCP_CONN_TABLE {
            const val TCP_CONN_STATE = ".1.3.6.1.2.1.6.13.1.1"
            const val TCP_CONN_LOCAL_ADDRESS = ".1.3.6.1.2.1.6.13.1.2"
            const val TCP_CONN_LOCAL_PORT = ".1.3.6.1.2.1.6.13.1.3"
            const val TCP_CONN_REM_ADDRESS = ".1.3.6.1.2.1.6.13.1.4"
            const val TCP_CONN_REM_PORT = ".1.3.6.1.2.1.6.13.1.5"
        }

        const val TCP_IN_ERRS = ".1.3.6.1.2.1.6.14.0"
        const val TCP_OUT_RSTS = ".1.3.6.1.2.1.6.15.0"
    }

    object UDP {
        const val UDP_IN_DATAGRAMS = ".1.3.6.1.2.1.7.1.0"
        const val UDP_NO_PORTS = ".1.3.6.1.2.1.7.2.0"
        const val UDP_IN_ERRORS = ".1.3.6.1.2.1.7.3.0"
        const val UDP_OUT_DATAGRAMS = ".1.3.6.1.2.1.7.4.0"

        object UDP_TABLE {
            const val UDP_LOCAL_ADDRESS = ".1.3.6.1.2.1.7.5.1.1"
            const val UDP_LOCAL_PORT = ".1.3.6.1.2.1.7.5.1.2"
        }
    }

    object INTERFACE {
        const val IF_DESCR = ".1.3.6.1.2.1.2.2.1.2"
        const val IF_TYPE = ".1.3.6.1.2.1.2.2.1.3"
        const val IF_SPEED = ".1.3.6.1.2.1.2.2.1.5"
        const val IF_PHYS_ADDRESS = ".1.3.6.1.2.1.2.2.1.6"
        const val IF_ADMIN_STATUS = ".1.3.6.1.2.1.2.2.1.7"
        const val IF_OPER_STATUS = ".1.3.6.1.2.1.2.2.1.8"
        const val IF_IN_OCTETS = ".1.3.6.1.2.1.2.2.1.10"
        const val IF_OU_OCTETS = ".1.3.6.1.2.1.2.2.1.16"
    }

    object SNMP {
        const val snmpInPkts = ".1.3.6.1.2.1.11.1.0"
        const val snmpOutPkts = ".1.3.6.1.2.1.11.2.0"
        const val snmpInBadVersions = ".1.3.6.1.2.1.11.3.0"
        const val snmpInBadCommunityNames = ".1.3.6.1.2.1.11.4.0"
        const val snmpInBadCommunityUses = ".1.3.6.1.2.1.11.5.0"
        const val snmpInASNParseErrs = ".1.3.6.1.2.1.11.6.0"
        const val snmpInTooBigs = ".1.3.6.1.2.1.11.8.0"
        const val snmpInNoSuchNames = ".1.3.6.1.2.1.11.9.0"
        const val snmpInBadValues = ".1.3.6.1.2.1.11.10.0"
        const val snmpInReadOnlys = ".1.3.6.1.2.1.11.11.0"
        const val snmpInGenErrs = ".1.3.6.1.2.1.11.12.0"
        const val snmpInTotalReqVars = ".1.3.6.1.2.1.11.13.0"
        const val snmpInTotalSetVars = ".1.3.6.1.2.1.11.14.0"
        const val snmpInGetRequests = ".1.3.6.1.2.1.11.15.0"
        const val snmpInGetNexts = ".1.3.6.1.2.1.11.16.0"
        const val snmpInSetRequests = ".1.3.6.1.2.1.11.17.0"
        const val snmpInGetResponses = ".1.3.6.1.2.1.11.18.0"
        const val snmpInTraps = ".1.3.6.1.2.1.11.19.0"

        ///////////////////////////////////////////////
        const val snmpOutTooBigs = ".1.3.6.1.2.1.11.20.0"
        const val snmpOutNoSuchNames = ".1.3.6.1.2.1.11.21.0"
        const val snmpOutBadValues = ".1.3.6.1.2.1.11.22.0"
        const val snmpOutGenErrs = ".1.3.6.1.2.1.11.24.0"
        const val snmpOutGetRequests = ".1.3.6.1.2.1.11.25.0"
        const val snmpOutGetNexts = ".1.3.6.1.2.1.11.26.0"
        const val snmpOutSetRequests = ".1.3.6.1.2.1.11.27.0"
        const val snmpOutGetResponses = ".1.3.6.1.2.1.11.28.0"
        const val snmpOutTraps = ".1.3.6.1.2.1.11.29.0"
        const val snmpEnableAuthenTraps = ".1.3.6.1.2.1.11.30.0"

    }


    object IP {
        const val ipForwarding = ".1.3.6.1.2.1.4.1.0"
        const val ipDefaultTTL = ".1.3.6.1.2.1.4.2.0"
        const val ipInReceives = ".1.3.6.1.2.1.4.3.0"
        const val ipInHdrErrors = ".1.3.6.1.2.1.4.4.0"
        const val ipInAddrErrors = ".1.3.6.1.2.1.4.5.0"
        const val ipForwDatagrams = ".1.3.6.1.2.1.4.6.0"
        const val ipInUnknownProtos = ".1.3.6.1.2.1.4.7.0"
        const val ipInDiscards = ".1.3.6.1.2.1.4.8.0"
        const val ipInDelivers = ".1.3.6.1.2.1.4.9.0"
        const val ipOutRequests = ".1.3.6.1.2.1.4.10.0"
        const val ipOutDiscards = ".1.3.6.1.2.1.4.11.0"
        const val ipOutNoRoutes = ".1.3.6.1.2.1.4.12.0"
        const val ipReasmTimeout = ".1.3.6.1.2.1.4.13.0"
        const val ipReasmReqds = ".1.3.6.1.2.1.4.14.0"
        const val ipReasmOKs = ".1.3.6.1.2.1.4.15.0"
        const val ipReasmFails = ".1.3.6.1.2.1.4.16.0"
        const val ipFragOKs = ".1.3.6.1.2.1.4.17.0"
        const val ipFragFails = ".1.3.6.1.2.1.4.18.0"
        const val ipFragCreates = ".1.3.6.1.2.1.4.19.0"
        const val ipRoutingDiscards = ".1.3.6.1.2.1.4.23.0"

        object IP_TABLE {

            const val ipAdEntAddr = ".1.3.6.1.2.1.4.20.1.1"
            const val ipAdEntNetMask = ".1.3.6.1.2.1.4.20.1.3"
            const val ipAdEntBcastAddr = ".1.3.6.1.2.1.4.20.1.4"
            const val ipAdEntReasmMaxSize = ".1.3.6.1.2.1.4.20.1.5"
        }
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
