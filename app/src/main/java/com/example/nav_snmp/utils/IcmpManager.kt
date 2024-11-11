package com.example.nav_snmp.utils

import android.content.Context
import android.util.Log
import com.example.nav_snmp.data.model.HostModelClass
import com.example.nav_snmp.ui.viewmodel.DescubrirHostViewModel
import com.marsounjan.icmp4a.Icmp
import com.marsounjan.icmp4a.Icmp4a
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.atomic.AtomicInteger

class IcmpManager {
    suspend fun pruebaConexionTCP(
        direccionesIp: List<String>,
        hostModel: HostModelClass
    ): HashMap<String, HostModelClass> = coroutineScope {
        val hostsAlcanzables = hashMapOf<String, HostModelClass>()

        val deferredResults = direccionesIp.map { ip ->
            async(Dispatchers.IO) {
                try {
                    // Intenta abrir una conexión TCP al puerto 80 (HTTP)
                    Socket().use { socket ->
                        socket.connect(InetSocketAddress(ip, 80), 2000) // 2000 ms de timeout
                        ip to hostModel.copy(direccionIP = ip)
                    }
                } catch (e: Exception) {
                    // Error significa que no se pudo conectar
                    Log.e("TCP Error", "No se pudo conectar a $ip en el puerto 80", e)
                    null
                }
            }
        }

        deferredResults.awaitAll().filterNotNull().forEach { (ip, host) ->
            hostsAlcanzables[ip] = host
        }

        hostsAlcanzables
    }

    suspend fun pruebaConexionICMP(
        direccionesIp: List<String>,
        hostModel: HostModelClass
    ): HashMap<String, HostModelClass> = coroutineScope {
        val hostsAlcanzables = hashMapOf<String, HostModelClass>()

        // Usar coroutines para realizar el ping en paralelo
        val deferredResults = direccionesIp.map { ip ->
            async(Dispatchers.IO) {
                try {
                    val address = InetAddress.getByName(ip)
                    // Intentar hacer ping y verificar si la dirección IP es alcanzable
                    if (address.isReachable(2000)) { // 2000 ms (2 segundos) de timeout
                        ip to hostModel.copy(direccionIP = ip)
                    } else {
                        null
                    }
                } catch (e: Exception) {
                    Log.e("ICMP Error", "Error en prueba de conexión ICMP con $ip", e)
                    null
                }
            }
        }

        // Filtrar los resultados nulos y añadir al HashMap
        deferredResults.awaitAll().filterNotNull().forEach { (ip, host) ->
            hostsAlcanzables[ip] = host
        }

        hostsAlcanzables
    }

    suspend fun pruebaConexionPing(
        direccionesIp: List<String>,
        hostModel: HostModelClass,
        intervaloMillis: Long = 1000L, // Intervalo por defecto de 1 segundo
        count: Int = 5 // Número de intentos por dirección IP
    ): HashMap<String, HostModelClass> = coroutineScope {
        val hostsAlcanzables = hashMapOf<String, HostModelClass>()

        val icmp = Icmp4a() // Instancia del objeto Icmp4a

        // Mapeamos cada IP en la lista de direcciones para hacer ping
        val deferredResults = direccionesIp.map { ip ->
            async(Dispatchers.IO) {
                try {
                    // Realizamos el ping con intervalo
                    for (i in 1..count) {
                        val status = icmp.ping(ip)
                        val result = status.result

                        when (result) {
                            is Icmp.PingResult.Success -> {
                                // Si el ping fue exitoso, añadir la IP al HashMap
                                hostsAlcanzables[ip] = hostModel.copy(direccionIP = ip)
                                Log.d("ICMP", "Ping exitoso a $ip, intento $i: ${result.ms} ms")
                            }
                            is Icmp.PingResult.Failed -> {
                                // Si el ping falló, registrar el error
                                Log.e("ICMP Error", "Error en intento $i con $ip: ${result.message}")
                            }
                        }

                        // Esperamos el intervalo entre intentos
                        delay(intervaloMillis)
                    }
                } catch (error: Icmp.Error.UnknownHost) {
                    Log.d("ICMP", "Unknown host $ip")
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
        Log.d("ICMP", "Direcciones IP generadas: $direccionesIp")
        val direccionesAlcanzables = pruebaConexionPing(direccionesIp, hostModel).keys.toList()
        return direccionesAlcanzables.map { ip ->
            hostModel.copy(
                direccionIP = ip,
                nombreHost = "Génerico",
                tipoDeDispositivo = TipoDispositivo.OTRO.name,
                comunidadSNMP = "public",
                puertoSNMP = 161,
            )
        }
//
//        DescubrirHostViewModel.hostIntentados.hostIntentados = direccionesIp.size
//
//        val listaHosts = mutableListOf<HostModelClass>()
//
//        coroutineScope {
//            var counter = AtomicInteger(0)
//
//            val deferredResults = direccionesAlcanzables.map { ip ->
//                async(Dispatchers.IO) {
//                    val id = counter.getAndIncrement()
//                    val host = hostModel.copy(direccionIP = ip, id = id)
//
//                    try {
//                        val map: HashMap<String, Any> = pruebaConexion(host)
//
//                        if (map["estado"] == true) {
//                            return@async map["host"] as HostModelClass
//                        }
//                    } catch (e: Exception) {
//                        Log.e("SNMP Error", "Error en pruebaConexionSnmp con $ip", e)
//                    }
//
//                    return@async null
//                }
//            }
//
//            listaHosts.addAll(deferredResults.awaitAll().filterNotNull())
//        }

//        return listaHosts
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