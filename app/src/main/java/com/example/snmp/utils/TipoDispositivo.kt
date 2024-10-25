package com.example.snmp.utils

import com.example.snmp.R

enum class TipoDispositivo {
    ROUTER,
    SWITCH,
    HOST,
    IMPRESORA,
    SERVIDOR,
    OTRO
}

class RecursoTipoDispositivo {

    fun obtenerRecurso(tipoDispositivo: TipoDispositivo): Int {
        return when (tipoDispositivo) {
            TipoDispositivo.ROUTER -> R.drawable.router
            TipoDispositivo.SWITCH -> R.drawable.network_switch
            TipoDispositivo.HOST -> R.drawable.host
            TipoDispositivo.IMPRESORA -> R.drawable.printer
            TipoDispositivo.SERVIDOR -> R.drawable.server
            TipoDispositivo.OTRO -> R.drawable.host

        }
    }

    fun obtenerRecurso(tipoDispositivo: String): Int {
        return when (tipoDispositivo) {
            TipoDispositivo.ROUTER.toString() -> R.drawable.router
            TipoDispositivo.SWITCH.toString() -> R.drawable.network_switch
            TipoDispositivo.HOST.toString() -> R.drawable.host
            TipoDispositivo.IMPRESORA.toString() -> R.drawable.printer
            TipoDispositivo.SERVIDOR.toString() -> R.drawable.server
            TipoDispositivo.OTRO.toString() -> R.drawable.host
            else -> {
                R.drawable.host
            }
        }
    }
}
