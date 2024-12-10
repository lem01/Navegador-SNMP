package com.example.nav_snmp.utils

import android.content.Context
import android.util.Log
import com.example.nav_snmp.data.model.HostModelClass

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket

class IcmpManager {
    suspend fun isNetworkDeviceReachable(addr: String, timeOutMillis: Int): Boolean {
        val networkPorts = listOf(
            80,
            443,
            22,
            23,
            161,
            53,
            135,
            445,
            3389,
            5900
        ) // Puertos comunes en routers, switches y hosts
        for (port in networkPorts) {
            if (isHostReachable(addr, port, timeOutMillis)) {
                return true
            }
        }
        return false
    }

    private suspend fun isHostReachable(addr: String, openPort: Int, timeOutMillis: Int): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                Socket().use { socket ->
                    socket.connect(InetSocketAddress(addr, openPort), timeOutMillis)
                }
                true
            } catch (ex: IOException) {
                false
            }
        }
    }


    suspend fun pruebaConexion(
        direccionesIp: List<String>,
        hostModel: HostModelClass
    ): HashMap<String, HostModelClass> = coroutineScope {
        val timeOutMillis = 500
        val hostsAlcanzables = hashMapOf<String, HostModelClass>()

        val deferredResults = direccionesIp.map { ip ->
            async(Dispatchers.IO) {
                try {

                    val isReachable = isNetworkDeviceReachable(ip, timeOutMillis)

                    if (isReachable) {
                        Log.d(
                            "ICMP",
                            "El dispositivo en $ip es alcanzable en uno de los puertos de administración o servicio."
                        )
                        hostsAlcanzables[ip] = hostModel.copy(direccionIP = ip)
                    } else {
                        Log.d(
                            "ICMP",
                            "Error: El dispositivo en $ip no es accesible en los puertos comunes."
                        )
                        null
                    }

                } catch (e: Exception) {
                    null
                }
            }
        }

        // Esperamos a que todos los resultados asíncronos terminen
        deferredResults.awaitAll()

        // Retornamos la lista de hosts alcanzables
        hostsAlcanzables
    }


    suspend fun descubrirHost(
        hostModel: HostModelClass,
        context: Context
    ): List<HostModelClass> {
        val direccionesIp = generarDireccionesPosibles(hostModel)
        var count = 1
        val direccionesAlcanzables = pruebaConexion(direccionesIp, hostModel).keys.toList()
        return direccionesAlcanzables.map { ip ->
            count += 1
            hostModel.copy(
                id = count,
                direccionIP = ip,
                nombreHost = "Génerico",
                tipoDeDispositivo = TipoDispositivo.OTRO.name,
                comunidadSNMP = "public",
                puertoSNMP = 161,
            )
        }

    }

    fun generarDireccionesPosibles(hostModel: HostModelClass): List<String> {
        val rangoIp = InetAddress.getByName(hostModel.direccionIP) // Usar la IP de hostModel
        val subRed = InetAddress.getByName("255.255.255.0")

        val rangoIpLong = ipToLong(rangoIp)
        val subRedLong = ipToLong(subRed)

        val hostCount = getHostCount("255.255.255.0")

        val endIp = (rangoIpLong and subRedLong) + hostCount - 1 // Dirección IP de broadcast

        return (rangoIpLong..endIp)
            .map { longToIp(it) }
            .filter { it != longToIp(endIp) } // Excluir solo la IP de broadcast
    }

    fun realizarPing(direcciones: List<String>): List<String> {
        return direcciones.filter { ip ->
            try {
                val inetAddress = InetAddress.getByName(ip)
                inetAddress.isReachable(1000) // Realizar ping con un tiempo de espera de 1000 ms
            } catch (e: Exception) {
                false // En caso de excepción, marcar como no alcanzable
            }
        }
    }

    fun ipToLong(ip: InetAddress): Long {
        val bytes = ip.address
        return bytes.foldIndexed(0L) { index, acc, byte ->
            acc or (byte.toLong() and 0xFF shl (8 * (3 - index)))
        }
    }

    fun longToIp(long: Long): String {
        return (0..3).joinToString(".") {
            ((long shr (8 * (3 - it))) and 0xFF).toString()
        }
    }

    fun getHostCount(subnetMask: String): Long {
        val maskParts = subnetMask.split(".").map { it.toInt() }
        val subnetBits = maskParts.sumOf { Integer.bitCount(it) }
        return 1L shl (32 - subnetBits) // 2^(32 - bits de red)
    }


}