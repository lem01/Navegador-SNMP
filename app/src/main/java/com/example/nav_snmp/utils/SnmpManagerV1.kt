package com.example.nav_snmp.utils

import CustomProgressDialog
import android.content.Context
import android.widget.Toast
import com.example.nav_snmp.data.model.HostModel
import id.ionbit.ionalert.IonAlert
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.snmp4j.CommunityTarget
import org.snmp4j.PDU
import org.snmp4j.Snmp
import org.snmp4j.TransportMapping
import org.snmp4j.mp.SnmpConstants
import org.snmp4j.smi.Address
import org.snmp4j.smi.GenericAddress
import org.snmp4j.smi.OID
import org.snmp4j.smi.OctetString
import org.snmp4j.smi.VariableBinding
import org.snmp4j.transport.DefaultUdpTransportMapping
import java.net.InetAddress


class SnmpManagerV1 : SnmpManagerInterface {
    private var snmp: Snmp? = null

    override fun snmpTest(hostModel: HostModel, context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            lateinit var customProgressDialog: CustomProgressDialog
            CoroutineScope(Dispatchers.Main).launch {
                customProgressDialog =
                    CustomProgressDialog(context, "Conectando", "Conectando con el dispositivo")
                customProgressDialog.show()

            }

            delay(500)

            try {

                val transport: TransportMapping<*> = DefaultUdpTransportMapping()
                snmp = Snmp(transport)
                snmp?.listen()

                val targetAddress: Address =
                    GenericAddress.parse("udp:${hostModel.direccionIP}/${hostModel.puertoSNMP}")

                val communityTarget = CommunityTarget<Address>().apply {
                    address = targetAddress
                    community = OctetString(hostModel.comunidadSNMP)
                    version = SnmpConstants.version1
                    retries = 2
                    timeout = 500
                }

                val pdu = PDU().apply {
                    type = PDU.GETNEXT
                    add(VariableBinding(OID("1.3.6.1.2.1.1")))
                }

                val response = snmp?.send(pdu, communityTarget)

                if (response?.response == null)
                    throw Exception("No respuesta recibida, la solicitud agotó el tiempo.")

                if (response.response.errorStatus == PDU.noError) {

                    mensajeAlert(context, "Correcto", "Conexión exitosa V1", IonAlert.SUCCESS_TYPE)

                    println("Respuesta: ${response.response.get(0)}")

                    customProgressDialog.dismiss()

                }
                close()
            } catch (e: Exception) {
                customProgressDialog.dismiss()

                e.printStackTrace()
                mensajeAlert(context, "Advertencia", "${e.message}", IonAlert.WARNING_TYPE)
                close()
            }
        }
    }

    override fun close() {
        try {
            snmp?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getOid() {
        TODO("Not yet implemented")
    }

    override fun setOid() {
        TODO("Not yet implemented")
    }

    override suspend fun descubrirHost(hostModel: HostModel, context: Context): List<HostModel> {
        val direccionesIp = generarDireccionesPosibles(hostModel)
        val direccionesAlcanzables = realizarPing(direccionesIp)

        var listHost = mutableListOf<HostModel>()

        direccionesIp.map { ip ->
            val host = hostModel.copy(direccionIP = ip)
            if (pruebaConexionSnmp(host, context))
                listHost.add(host)
        }

        println("direcciones ip: $direccionesIp")
        println("Direcciones alcanzables: $direccionesAlcanzables")

        return listHost
    }

    override fun mensajeAlert(context: Context, s: String, s1: String, successType: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            IonAlert(context, successType)
                .setTitleText(s)
                .setContentText(s1)
                .show()
        }
    }

    fun generarDireccionesPosibles(hostModel: HostModel): List<String> {
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

    suspend fun pruebaConexionSnmp(hostModel: HostModel, context: Context): Boolean {
        return try {
            withContext(Dispatchers.IO) {
                val transport: TransportMapping<*> = DefaultUdpTransportMapping()
                snmp = Snmp(transport)
                snmp?.listen()

                val targetAddress: Address =
                    GenericAddress.parse("udp:${hostModel.direccionIP}/${hostModel.puertoSNMP}")

                val communityTarget = CommunityTarget<Address>().apply {
                    address = targetAddress
                    community = OctetString(hostModel.comunidadSNMP)
                    version = SnmpConstants.version1
                    retries = 2
                    timeout = 100
                }

                val pdu = PDU().apply {
                    type = PDU.GET
                    add(VariableBinding(OID(CommonOids.SYS_DESCR)))
                }

                val response = snmp?.send(pdu, communityTarget)

                if (response?.response == null) {
                    throw Exception("No respuesta recibida, la solicitud agotó el tiempo.")
                }

                println("Respuesta: ${response.response.get(0)}")
               return@withContext response.response.errorStatus == PDU.noError

            }
        } catch (e: Exception) {
            e.printStackTrace()
            println("${e.message}")
            false
        } finally {
            close()
        }
    }

}


private suspend fun mensajeToast(context: Context, message: String) {
    withContext(Dispatchers.Main) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}



