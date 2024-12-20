//package com.example.snmp.utils
//
//import android.content.Context
//import android.widget.Toast
//import com.example.nav_snmp.data.model.HostModel
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import org.snmp4j.PDU
//import org.snmp4j.ScopedPDU
//import org.snmp4j.Snmp
//import org.snmp4j.TransportMapping
//import org.snmp4j.UserTarget
//import org.snmp4j.mp.SnmpConstants
//import org.snmp4j.security.SecurityLevel
//import org.snmp4j.smi.Address
//import org.snmp4j.smi.GenericAddress
//import org.snmp4j.smi.OID
//import org.snmp4j.smi.OctetString
//import org.snmp4j.smi.VariableBinding
//import org.snmp4j.transport.DefaultUdpTransportMapping
//
//class SnmpManagerV3 : SnmpManagerInterface {
//    private var snmp: Snmp? = null
//
//    override fun snmpTest(hostModel: HostModel, context: Context) {
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val transport: TransportMapping<*> = DefaultUdpTransportMapping()
//                snmp = Snmp(transport)
//                snmp?.listen()
//
//                val targetAddress: Address =
//                    GenericAddress.parse("udp:${hostModel.direccionIP}/${hostModel.puertoSNMP}")
//
//                val userTarget = UserTarget()
//
//
//                {
//                    address = targetAddress
//                    userTarget = OctetString(hostModel.comunidadSNMP)
//                    securityLevel = SecurityLevel.AUTH_PRIV
//                    retries = 2
//                    timeout = 1000
//                    version = SnmpConstants.version3
//                }
//
//                val pdu = ScopedPDU().apply {
//                    type = PDU.GETNEXT
//                    add(VariableBinding(OID("1.3.6.1.2.1.1")))
//                }
//
//                val response = snmp?.send(pdu, userTarget)
//
//                if (response?.response == null) {
//                    println("No respuesta recibida, la solicitud agotó el tiempo.")
//                } else if (response.response.errorStatus == PDU.noError) {
//                    mensajeToast(context, "Conexión exitosa")
//                    println("Respuesta: ${response.response.get(0)}")
//                }
//
//                close()
//            } catch (e: Exception) {
//                e.printStackTrace()
//                mensajeToast(context, "Error durante la solicitud SNMP: ${e.message}")
//                close()
//            }
//        }
//    }
//
//    override fun close() {
//        try {
//            snmp?.close()
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    private suspend fun mensajeToast(context: Context, message: String) {
//        withContext(Dispatchers.Main) {
//            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
//        }
//    }
//}
