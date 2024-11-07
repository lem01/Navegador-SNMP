package com.example.nav_snmp.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object Convertidor {

    //funcion que convierte un string a un entero

    fun convertirOctetStringAFecha(octetString: String): String {
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

            else -> {
                s
            }
        }

        return formater
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
