package com.example.nav_snmp.utils

import CustomProgressDialog
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import kotlinx.coroutines.*
import android.util.Log
import android.widget.Toast
import com.example.nav_snmp.data.model.HostModel
import com.example.nav_snmp.data.model.HostModelClass
import com.example.nav_snmp.ui.viewmodel.DescubrirHostViewModel
import id.ionbit.ionalert.IonAlert
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.snmp4j.CommunityTarget
import org.snmp4j.PDU
import org.snmp4j.Snmp
import org.snmp4j.TransportMapping
import org.snmp4j.event.ResponseEvent
import org.snmp4j.mp.SnmpConstants
import org.snmp4j.smi.Address
import org.snmp4j.smi.Counter32
import org.snmp4j.smi.GenericAddress
import org.snmp4j.smi.Integer32
import org.snmp4j.smi.OID
import org.snmp4j.smi.OctetString
import org.snmp4j.smi.VariableBinding
import org.snmp4j.transport.DefaultUdpTransportMapping
import java.io.IOException
import java.net.InetAddress
import java.util.concurrent.atomic.AtomicInteger
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class SnmpManagerV1 : SnmpManagerInterface {
    private val TAG = "SnmpManagerV1"
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
                    add(VariableBinding(OID(CommonOids.SYSTEM.SYS_NAME)))
                }

                val response = snmp?.send(pdu, communityTarget)

                if (response?.response == null)
                    throw Exception("No respuesta recibida, la solicitud agotó el tiempo.")

                if (response.response.errorStatus != PDU.noError) {
                    val errorStatusText = response.response.errorStatusText
//                    continuation.resumeWithException(Exception("Error en la respuesta SNMP: $errorStatusText"))
                    throw Exception(errorStatusText)
                }

                mensajeAlert(context, "Correcto", "Conexión exitosa V1", IonAlert.SUCCESS_TYPE)

                println("Respuesta: ${response.response.get(0)}")

                customProgressDialog.dismiss()

                close()
            } catch (e: Exception) {
                if (customProgressDialog.isShowing)
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


    override suspend fun getOid(vararg args: Any): String {
        val hostModel = args[0] as HostModel
        val oid = args[1] as String

        return suspendCoroutine { continuation ->
            val mensaje = ""
            CoroutineScope(Dispatchers.IO).launch {
                delay(200)
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

                    val pduType: Int = RecursoPdu().obtenerRecurso(TipoOperacion.GET)

                    val pdu = PDU().apply {
                        type = pduType
                        add(
                            VariableBinding(OID(oid))
                        )
                    }

                    val response = snmp?.send(pdu, communityTarget)

                    if (response?.response == null)
                        throw Exception("No respuesta recibida, la solicitud agotó el tiempo.")


                    if (response.response.errorStatus != PDU.noError) {
                        val errorStatusText = response.response.errorStatusText
                        throw Exception(errorStatusText)
                    }

                    val respuesta = response.response.get(0).oid.toString()
                    continuation.resume(respuesta)

                    close()
                } catch (e: Exception) {
                    e.printStackTrace()
//                    mensajeAlert(context, "Advertencia", "${e.message}", IonAlert.WARNING_TYPE)
                    Log.e("SNMP Error", "Error en getOid: ${e.message}", e)
                    close()
//                    continuation.resumeWithException(e)
                    continuation.resume("")
                }
            }
        }
    }

    override suspend fun operacionSnmp(vararg args: Any): Any {
//        val hostModel = args[0] as HostModel
//        val oid = args[1] as String
//        val tipoOperacion = args[2] as TipoOperacion
//        val context = args[3] as Context
//
//        when (tipoOperacion) {
//            TipoOperacion.GET -> {
//                get(hostModel, oid, tipoOperacion, context, true)
//            }
//            else -> {
//                getNext(hostModel, oid, tipoOperacion, context, true)
//            }
//        }
        return ""
    }


    override suspend fun get(
        hostModel: HostModel,
        oid: String,
        tipoOperacion: TipoOperacion,
        context: Context,
        isShowPgrogress: Boolean,
        showMensajeAdvertencia: Boolean
    ): String {
        if (isShowPgrogress)
            return suspendCoroutine { continuation ->
                val mensaje = ""
                CoroutineScope(Dispatchers.IO).launch {
                    lateinit var customProgressDialog: CustomProgressDialog
                    CoroutineScope(Dispatchers.Main).launch {
                        customProgressDialog =
                            CustomProgressDialog(context, "", "Cargando")
                        customProgressDialog.show()
                    }
                    delay(200)
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

                        val pduType: Int = RecursoPdu().obtenerRecurso(tipoOperacion)

                        val pdu = PDU().apply {
                            type = pduType
                            add(
                                VariableBinding(OID(oid))
                            )
                        }

                        val response = snmp?.send(pdu, communityTarget)

                        if (response?.response == null)
                            throw Exception("No respuesta recibida, la solicitud agotó el tiempo.")

                        if (response.response.errorStatus != PDU.noError) {
                            val errorStatusText = response.response.errorStatusText
//                            continuation.resumeWithException(Exception("Error en la respuesta SNMP: $errorStatusText"))
                            throw Exception(errorStatusText)
                        }

                        val respuesta = response.response.get(0).variable.toString()

                        customProgressDialog.dismiss()
                        continuation.resume(respuesta)
                        close()
                    } catch (e: Exception) {
                        if (customProgressDialog.isShowing)
                            customProgressDialog.dismiss()
                        e.printStackTrace()
                        if (showMensajeAdvertencia)
                            mensajeAlert(
                                context,
                                "Advertencia",
                                "${e.message}",
                                IonAlert.WARNING_TYPE
                            )
                        continuation.resume("")
                        close()
                    }
                }
            }

        return suspendCoroutine { continuation ->
            val mensaje = ""
            CoroutineScope(Dispatchers.IO).launch {
                delay(200)
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

                    val pduType: Int = RecursoPdu().obtenerRecurso(tipoOperacion)

                    val pdu = PDU().apply {
                        type = pduType
                        add(
                            VariableBinding(OID(oid))
                        )
                    }

                    val response = snmp?.send(pdu, communityTarget)

                    if (response?.response == null)
                        throw Exception("No respuesta recibida, la solicitud agotó el tiempo.")


                    if (response.response.errorStatus != PDU.noError) {
                        val errorStatusText = response.response.errorStatusText
                        throw Exception(errorStatusText)
                    }

                    val respuesta = response.response.get(0).variable.toString()
                    continuation.resume(respuesta)

                    close()
                } catch (e: Exception) {
                    e.printStackTrace()
                    if (showMensajeAdvertencia)
                        mensajeAlert(context, "Advertencia", "${e.message}", IonAlert.WARNING_TYPE)
//                    continuation.resumeWithException(e)
                    continuation.resume("")
                    close()

                }
            }
        }
    }

    override suspend fun getNext(vararg args: Any): Any {
        val hostModel = args[0] as HostModel
        val oid = args[1] as String
        val tipoOperacion = args[2] as TipoOperacion
        val context = args[3] as Context
        val isShowProgress = args[4] as Boolean

        if (isShowProgress)
            return suspendCoroutine { continuation ->
                val mensaje = ""
                CoroutineScope(Dispatchers.IO).launch {
                    lateinit var customProgressDialog: CustomProgressDialog
                    CoroutineScope(Dispatchers.Main).launch {
                        customProgressDialog =
                            CustomProgressDialog(context, "", "Cargando")
                        customProgressDialog.show()
                    }
                    delay(200)
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

                        val pduType: Int = RecursoPdu().obtenerRecurso(tipoOperacion)

                        val pdu = PDU().apply {
                            type = pduType
                            add(
                                VariableBinding(OID(oid))
                            )
                        }

                        val response = snmp?.send(pdu, communityTarget)

                        if (response?.response == null)
                            throw Exception("No respuesta recibida, la solicitud agotó el tiempo.")

                        if (response.response.errorStatus != PDU.noError) {
                            val errorStatusText = response.response.errorStatusText
//                            continuation.resumeWithException(Exception("Error en la respuesta SNMP: $errorStatusText"))
                            throw Exception(errorStatusText)
                        }

                        val respuestaVariable = response.response.get(0).variable.toString()
                        val respuestaOID = response.response.get(0).oid.toString()
                        val map = hashMapOf("valor" to respuestaVariable, "oid" to respuestaOID)

                        customProgressDialog.dismiss()
                        continuation.resume(map)
                        close()
                    } catch (e: Exception) {
                        if (customProgressDialog.isShowing)
                            customProgressDialog.dismiss()
                        e.printStackTrace()
                        mensajeAlert(context, "Advertencia", "${e.message}", IonAlert.WARNING_TYPE)
                        close()
                        val map = hashMapOf("valor" to "", "oid" to "")
                        continuation.resume(map)
                    }
                }
            }

        return suspendCoroutine { continuation ->
            CoroutineScope(Dispatchers.IO).launch {
                delay(200)
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

                    val pduType: Int = RecursoPdu().obtenerRecurso(tipoOperacion)

                    val pdu = PDU().apply {
                        type = pduType
                        add(VariableBinding(OID(oid)))
                    }

                    val response = snmp?.send(pdu, communityTarget)

                    if (response?.response == null)
                        throw Exception("No respuesta recibida, la solicitud agotó el tiempo.")

                    if (response.response.errorStatus != PDU.noError) {
                        val errorStatusText = response.response.errorStatusText
                        throw Exception(errorStatusText)
                    }

                    val respuestaVariable = response.response.get(0).variable.toString()
                    val respuestaOID = response.response.get(0).oid.toString()
                    val map = hashMapOf("valor" to respuestaVariable, "oid" to respuestaOID)

                    continuation.resume(map)
                    close()
                } catch (e: Exception) {
                    e.printStackTrace()
                    mensajeAlert(context, "Advertencia", "${e.message}", IonAlert.WARNING_TYPE)
                    val map = hashMapOf("valor" to "", "oid" to "")
                    continuation.resume(map)
                }
            }
        }
    }

    override suspend fun set(vararg args: Any): Any {
        val hostModel = args[0] as HostModel
        val oid = args[1] as String
        val valorNuevo = args[2]
        val tipoVariable = args[3] as String
        val context = args[4] as Context

        return suspendCoroutine { continuation ->
            val mensaje = ""
            CoroutineScope(Dispatchers.IO).launch {
                delay(200)
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
                        type = PDU.SET
                        add(getVariableBinding(tipoVariable, valorNuevo, oid))
                    }

                    val response = snmp?.send(pdu, communityTarget)

                    if (response?.response == null)
                        throw Exception("No respuesta recibida, la solicitud agotó el tiempo.")


                    if (response.response.errorStatus != PDU.noError) {
                        val errorStatusText = response.response.errorStatusText
                        throw Exception(errorStatusText)
                    }

                    val respuesta = response.response.get(0).variable.toString()
                    continuation.resume(respuesta)

                    close()
                } catch (e: Exception) {
                    e.printStackTrace()
                    CoroutineScope(Dispatchers.Main).launch {
                        showAlert(context, "Advertencia", "${e.message}")
                    }
//                    mensajeAlert(context, "Advertencia", "${e.message}", IonAlert.WARNING_TYPE)
                    close()
//                    continuation.resumeWithException(e)
                    continuation.resume("")
                }
            }
        }
    }

    private fun getVariableBinding(
        tipoVariable: String,
        valorNuevo: Any,
        oid: String
    ): VariableBinding {
        return when (tipoVariable) {
            TipoVariableSnmp.OCTETSTRING -> VariableBinding(
                OID(oid),
                OctetString(valorNuevo as String)
            )

            TipoVariableSnmp.INTEGER -> {
                VariableBinding(OID(oid), Integer32(valorNuevo as Int))
            }

            else -> VariableBinding(OID(oid), OctetString(valorNuevo as String))
        }

    }


    override suspend fun walk(
        hostModel: HostModel,
        oid: String,
        context: Context,
        isShowProgress: Boolean,
        showMensajeAdvertencia: Boolean
    ): List<String> {
        return suspendCoroutine { continuation ->
            val resultList = mutableListOf<String>()
            CoroutineScope(Dispatchers.IO).launch {
                lateinit var customProgressDialog: CustomProgressDialog
                if (isShowProgress) {
                    CoroutineScope(Dispatchers.Main).launch {
                        customProgressDialog = CustomProgressDialog(context, "", "Cargando")
                        customProgressDialog.show()
                    }
                }
                delay(200)

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
                        add(VariableBinding(OID(oid)))
                    }

                    var nextOid = OID(oid)
                    while (true) {
                        val response = snmp?.send(pdu, communityTarget)

                        if (response?.response == null || response.response.errorStatus != PDU.noError) {
                            break
                        }

                        val variableBinding = response.response.get(0)
                        val variableOid = variableBinding.oid

                        // Stop if we reach beyond the subtree of the initial OID
                        if (!variableOid.startsWith(OID(oid))) break

                        resultList.add(variableBinding.variable.toString())

                        // Update the PDU with the next OID for walking
                        nextOid = variableOid
                        pdu.clear()
                        pdu.add(VariableBinding(nextOid))
                    }

                    if (isShowProgress) {
                        customProgressDialog.dismiss()
                    }
                    continuation.resume(resultList)
                    close()
                } catch (e: Exception) {
                    if (isShowProgress) {
                        if (customProgressDialog.isShowing)
                            customProgressDialog.dismiss()
                    }
                    e.printStackTrace()
                    if (showMensajeAdvertencia)
                        mensajeAlert(context, "Advertencia", "${e.message}", IonAlert.WARNING_TYPE)
                    close()
                    continuation.resume(emptyList())
                }
            }
        }
    }

    fun showAlert(context: Context, title: String, message: String) {
        // Asegúrate de que el contexto sea de una actividad y que la actividad no se haya destruido
        if (context is Activity && !context.isFinishing) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle(title)
            builder.setMessage(message)
            builder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }

            // Crear y mostrar el diálogo
            val alertDialog = builder.create()
            alertDialog.show()
        }
    }

    suspend fun descubrirHost(hostModel: HostModelClass, context: Context): List<HostModelClass> {
        val direccionesIp = generarDireccionesPosibles(hostModel)
//        val direccionesAlcanzables = realizarPing(direccionesIp)

        DescubrirHostViewModel.hostIntentados.hostIntentados = direccionesIp.size

        val listaHosts = mutableListOf<HostModelClass>()

        // Usamos coroutineScope para esperar correctamente todas las coroutines
        coroutineScope {
            var counter: AtomicInteger = AtomicInteger(0)

            // Creamos una lista de coroutines usando async, todas se ejecutan en el dispatcher IO
            val deferredResults = direccionesIp.map { ip ->
                async(Dispatchers.IO) {
                    // Incrementar el contador de manera atómica para evitar problemas de concurrencia
                    val id = counter.getAndIncrement()
                    val host = hostModel.copy(direccionIP = ip, id = id)

                    try {
                        val map: HashMap<String, Any> = pruebaConexionSnmp(host)

                        // Verificamos el estado y retornamos el host si es válido
                        if (map["estado"] == true) {
                            return@async map["host"] as HostModelClass
                        }
                    } catch (e: Exception) {
                        Log.e("SNMP Error", "Error en pruebaConexionSnmp con $ip", e)
                    }

                    return@async null
                }
            }

            // Esperamos todos los resultados y filtramos los nulos
            listaHosts.addAll(deferredResults.awaitAll().filterNotNull())
        }

        return listaHosts
    }

    override fun mensajeAlert(context: Context, s: String, s1: String, successType: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            IonAlert(context, successType)
                .setTitleText(s)
                .setContentText(s1)
                .show()
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

    suspend fun pruebaConexionSnmp(hostModel: HostModelClass): HashMap<String, Any> {
        val map: HashMap<String, Any> = HashMap()
        var snmp: Snmp? = null // Evitar variables globales para asegurar que se libere siempre
        return try {
            withContext(Dispatchers.IO) {
                val transport: TransportMapping<*> = DefaultUdpTransportMapping()
                snmp = Snmp(transport)
                transport.listen() // Iniciar transporte

                val targetAddress: Address =
                    GenericAddress.parse("udp:${hostModel.direccionIP}/${hostModel.puertoSNMP}")

                val communityTarget = CommunityTarget<Address>().apply {
                    address = targetAddress
                    community = OctetString(hostModel.comunidadSNMP)
                    version = SnmpConstants.version1
                    retries = 2
                }

                // Intento de obtener el nombre del sistema
                val responseSysName =
                    enviarMensajeSnmp(snmp!!, communityTarget, CommonOids.SYSTEM.SYS_NAME)
                if (responseSysName.response == null || responseSysName.response.errorStatus != PDU.noError) {

                    return@withContext map.apply { put("estado", false) }
                }

                // Asignar nombre del host
                hostModel.nombreHost = responseSysName.response.get(0).variable.toString()
                if (hostModel.nombreHost.isBlank()) hostModel.nombreHost = "Genérico"

                // Intento de obtener el estado de reenvío de IP
                val responseIpForwarding =
                    enviarMensajeSnmp(snmp!!, communityTarget, CommonOids.IP.IP_FORWARDING)
                if (responseIpForwarding.response == null || responseIpForwarding.response.errorStatus != PDU.noError) {
                    Log.e(TAG, "Error en IP_FORWARDING: respuesta nula o estado de error")
                    return@withContext map.apply { put("estado", false) }
                }

                // Determinar tipo de dispositivo
                if (responseIpForwarding.response.get(0).variable.toString() == "1") {
                    hostModel.tipoDeDispositivo = TipoDispositivo.ROUTER.name
                }

                map.apply {
                    put("estado", true)
                    put("host", hostModel)
                }
            }
        } catch (e: RuntimeException) {
            Log.e(TAG, "RuntimeException: ${e.message}", e)
            map.apply { put("estado", false) }
        } catch (e: NullPointerException) {
            Log.e(TAG, "NullPointerException: ${e.message}", e)
            map.apply { put("estado", false) }
        } catch (e: Exception) {
            Log.e(TAG, "Exception: ${e.message}", e)
            map.apply { put("estado", false) }
        } finally {
            try {
                snmp?.close() // Cerrar siempre snmp para liberar recursos
            } catch (e: IOException) {
                Log.e(TAG, "Error cerrando SNMP: ${e.message}", e)
            }
        }
    }

    private fun enviarMensajeSnmp(
        snmp: Snmp,
        communityTarget: CommunityTarget<Address>,
        oid: String
    ): ResponseEvent<Address> {
        val pdu = PDU().apply {
            type = PDU.GETNEXT
            add(VariableBinding(OID(oid)))
        }

        return snmp.send(pdu, communityTarget)
    }


}


private suspend fun mensajeToast(context: Context, message: String) {
    withContext(Dispatchers.Main) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
