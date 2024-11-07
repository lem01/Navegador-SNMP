package com.example.nav_snmp.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
}
