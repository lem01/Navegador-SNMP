package com.example.nav_snmp.utils

import android.util.Log
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object Convertidor {

    //funcion que convierte un string a un entero

    fun convertirOctetStringAFecha(octetString: String): String {

        if (!octetString.contains(":")) {
            return octetString
        }

        // Separar el valor en bytes
        val bytes = octetString.split(":").map { it.toInt(16) }

        // Extraer cada componente de fecha y hora del octet string
        val year = (bytes[0] shl 8 or bytes[1]) // Año completo en formato BCD
        val month = bytes[2] and 0xFF // Mes
        val day = bytes[3] and 0xFF // Día
        val hour = bytes[4] and 0xFF // Hora
        val minute = bytes[5] and 0xFF // Minuto
        val second = bytes[6] and 0xFF // Segundo

        // Crear el objeto LocalDateTime
        val fecha = LocalDateTime.of(year, month, day, hour, minute, second)

        // Formatear la fecha en el formato de Nicaragua: dd/MM/yyyy HH:mm:ss
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
        return fecha.format(formatter)
    }

    fun getFormater(s: String, oid: String): String {
        var formater = ""
        formater = when (oid) {

            CommonOids.HOST.HR_SYSTEM_DATE -> {
                convertirOctetStringAFecha(s)
            }

            CommonOids.INTERFACE.IF_DESCR -> {
                octetStringToReadableString(s)
            }

            CommonOids.INTERFACE.IF_TYPE -> {
                getInterfaceDescriptionType(s.toInt())
            }

            CommonOids.INTERFACE.IF_OPER_STATUS -> {
                getInterfacesDescriptionStatusOper(s.toInt())
            }

            CommonOids.INTERFACE.IF_ADMIN_STATUS -> {
                getInterfacesDescriptionAdminStatus(s.toInt())
            }

            else -> {
                s
            }
        }

        return formater
    }

    fun getFormater2(s: String, oid: String): String {
//        Log.d("Recibido: ", "Recibido OID: $oid") // Para ver cada OID que se procesa
//        Log.d(
//            "Esperado prefijo",
//            "Esperado prefijo: ${CommonOids.INTERFACE.IF_DESCR}"
//        ) // Para verificar el prefijo

        val oidNormalizer = if (oid.startsWith(".")) oid else ".$oid"
        return when {
            oidNormalizer.startsWith(CommonOids.INTERFACE.IF_DESCR) -> {
                octetStringToReadableString(s)
            }

            oidNormalizer == CommonOids.INTERFACE.IF_DESCR -> octetStringToReadableString(s)

            oidNormalizer == CommonOids.HOST.HR_SYSTEM_DATE -> convertirOctetStringAFecha(s)
            oidNormalizer == CommonOids.INTERFACE.IF_TYPE -> getInterfaceDescriptionType(s.toInt())
            oidNormalizer == CommonOids.INTERFACE.IF_OPER_STATUS -> getInterfacesDescriptionStatusOper(
                s.toInt()
            )

            oidNormalizer == CommonOids.INTERFACE.IF_ADMIN_STATUS -> getInterfacesDescriptionAdminStatus(
                s.toInt()
            )

            else -> s
        }
    }

    private fun getInterfacesDescriptionAdminStatus(toInt: Int): String {
        return when (toInt) {
            1 -> "Up ($toInt)"               // La interfaz está habilitada y activa
            2 -> "Down ($toInt)"             // La interfaz está administrativamente deshabilitada
            3 -> "Testing ($toInt)"          // La interfaz está en estado de prueba
            4 -> "Unknown ($toInt)"          // Estado desconocido o no disponible
            5 -> "Dormant ($toInt)"          // La interfaz está inactiva pero no completamente apagada
            6 -> "Not Present ($toInt)"      // La interfaz no está físicamente presente
            7 -> "Lower Layer Down ($toInt)" // Capa inferior inactiva
            else -> "Unknown Status ($toInt)" // Estado no reconocido
        }
    }

    private fun getInterfacesDescriptionStatusOper(toInt: Int): String {
        return when (toInt) {
            1 -> "Up ($toInt)"               // La interfaz está operativa
            2 -> "Down ($toInt)"             // La interfaz está inactiva
            3 -> "Testing ($toInt)"          // La interfaz está en estado de prueba
            4 -> "Unknown ($toInt)"          // Estado operativo desconocido
            5 -> "Dormant ($toInt)"          // La interfaz está inactiva pero puede activarse
            6 -> "Not Present ($toInt)"      // La interfaz no está físicamente presente en el dispositivo
            7 -> "Lower Layer Down ($toInt)" // Capa inferior de la interfaz está inactiva
            else -> "Unknown Status ($toInt)" // Estado no reconocido
        }

    }

    fun getInterfaceDescriptionType(ifType: Int): String {
        return when (ifType) {
            1 -> "Other ($ifType)"                           // Otro tipo no especificado
            6 -> "Ethernet CSMACD ($ifType)"                 // Ethernet CSMACD (Carrier Sense Multiple Access with Collision Detection)
            24 -> "Software Loopback ($ifType)"              // Software Loopback (para pruebas internas)
            53 -> "Prop Point to Point Serial ($ifType)"     // Serial punto a punto
            23 -> "PPP ($ifType)"                            // Point-to-Point Protocol (PPP)
            62 -> "ISDN ($ifType)"                           // ISDN (Integrated Services Digital Network)
            71 -> "Ieee80211 ($ifType)"
            117 -> "Gigabit Ethernet ($ifType)"              // Ethernet a 1 Gbps
            131 -> "Tunnel ($ifType)"                        // Tunnel (interfaz de túnel)
            150 -> "DSL ($ifType)"                           // Digital Subscriber Line
            161 -> "ATM ($ifType)"                           // Asynchronous Transfer Mode (ATM)
            166 -> "Ethernet 3Mbps ($ifType)"                // Ethernet a 3 Mbps
            170 -> "Fast Ethernet (100BaseT) ($ifType)"      // Fast Ethernet a 100 Mbps
            176 -> "DOCSIS Cable MacLayer ($ifType)"         // Cable Modem DOCSIS
            177 -> "DOCSIS Cable Upstream ($ifType)"         // Upstream de Cable Modem DOCSIS
            180 -> "ATM (logical) ($ifType)"                 // ATM Lógica
            181 -> "DOCSIS Cable Downstream ($ifType)"       // Downstream de Cable Modem DOCSIS
            209 -> "ATM Virtual ($ifType)"                   // ATM Virtual
            210 -> "MPLS ($ifType)"                          // Multiprotocol Label Switching (MPLS)
            226 -> "Stacked VLAN ($ifType)"                  // VLAN apilada (Stacked)
            237 -> "VPLS ($ifType)"                          // Virtual Private LAN Service (VPLS)
            253 -> "HyperSCSI ($ifType)"                     // HyperSCSI
            255 -> "Other Network Interface ($ifType)"       // Otra interfaz de red
            else -> "Unknown ($ifType)"                       // Tipo desconocido
        }
    }

    fun octetStringToReadableString(octetString: String): String {
        try {
            if (!octetString.contains(":")) {
                return octetString
            }

            // Convertimos cada par hexadecimal a un byte y los almacenamos en un array de bytes
            val bytes = octetString.split(":").map { it.toInt(16).toByte() }.toByteArray()

            // Convertimos el array de bytes a una cadena UTF-8
            return String(bytes, Charsets.UTF_8)
        } catch (e: Exception) {
            return octetString
        }
    }

    fun convertToGigabytes(unidadesAsignacion: String, tamanios: String): Double {
        val totalBytes = unidadesAsignacion.toDouble() * tamanios.toDouble()
        val totalGigabytes =
            totalBytes / (1024.0 * 1024 * 1024)  // Usamos 1024.0 para hacer la división como Double
        return totalGigabytes
    }

    fun formatDoubleToString(numbers: List<Double>): List<String> {
        val symbols = DecimalFormatSymbols(Locale.US)
        val oneDecimalFormat = DecimalFormat("#.#", symbols)
        val twoDecimalFormat = DecimalFormat("#.##", symbols)

        return numbers.map { number ->
            val decimalPart = number - number.toInt()  // Obtiene solo la parte decimal
            if (decimalPart in 0.00..0.09) {
                oneDecimalFormat.format(number)  // Si la parte decimal está entre 0.00 y 0.09, formatea con un solo decimal
            } else {
                twoDecimalFormat.format(number)  // Si no, formatea con dos decimales
            }
        }
    }

}
