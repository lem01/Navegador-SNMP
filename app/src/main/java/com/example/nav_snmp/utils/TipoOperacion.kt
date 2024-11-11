package com.example.nav_snmp.utils

import org.snmp4j.PDU

enum class TipoOperacion {
    GET, GET_NEXT, SET
}


class RecursoPdu {

    fun obtenerRecurso(tipoOperacion: TipoOperacion): Int {
        return when (tipoOperacion) {
            TipoOperacion.GET -> PDU.GET
            TipoOperacion.GET_NEXT -> PDU.GETNEXT
            else -> PDU.GET
        }
    }
}
