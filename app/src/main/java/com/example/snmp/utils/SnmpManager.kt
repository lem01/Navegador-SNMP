package com.example.snmp.utils

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.snmp4j.CommunityTarget
import org.snmp4j.PDU
import org.snmp4j.Snmp
import org.snmp4j.TransportMapping
import org.snmp4j.event.ResponseEvent
import org.snmp4j.fluent.SnmpCompletableFuture
import org.snmp4j.fluent.TargetBuilder
import org.snmp4j.mp.MPv3
import org.snmp4j.mp.SnmpConstants
import org.snmp4j.security.AuthMD5
import org.snmp4j.security.PrivAES128
import org.snmp4j.security.SecurityModels
import org.snmp4j.security.SecurityModels.*
import org.snmp4j.security.SecurityProtocols
import org.snmp4j.security.USM
import org.snmp4j.security.UsmUser
import org.snmp4j.smi.Address
import org.snmp4j.smi.GenericAddress
import org.snmp4j.smi.OID
import org.snmp4j.smi.OctetString
import org.snmp4j.smi.VariableBinding
import org.snmp4j.transport.DefaultUdpTransportMapping
import java.util.concurrent.ExecutionException
import org.snmp4j.fluent.SnmpBuilder as SnmpBuilder


class SnmpManager(
//    private val ipAddress: String,
//    private val community: String,
//    private val port: Int = 161
) {
    private var snmp: Snmp? = null

//    init {
//
//        try {
//            val snmp = Snmp(DefaultUdpTransportMapping())
//            snmp.listen()
//        } catch (e: Exception) {
//            e.printStackTrace()
//
//        }
//    }
//
//    fun getAsString(oid: String): String? {
//        try {
//
//            val target = CommunityTarget<Address>().apply {
//                address = GenericAddress.parse("udp:$ipAddress/$port")
//                community = OctetString(this@SnmpManager.community)
//                version = SnmpConstants.version2c
//                retries = 2
//                timeout = 1500
//            }
//
//            val pdu = PDU().apply {
//                type = PDU.GET
//                add(VariableBinding(OID(oid)))
//            }
//
////            val response: ResponseEvent<*> = snmp!!.send<Address>(requestPDU, target)
//
//            val response: ResponseEvent<*> = snmp!!.send<Address>(pdu, target)
//
//            response.let {
//                if (it.response != null) {
//                    return it.response.variableBindings.firstOrNull()?.toValueString()
//                } else {
//                    println("Error: Response is null")
//                }
//            }
//            pdu.type = PDU.INFORM
//
//// sysUpTime
//            val sysUpTime: Long = (System.nanoTime() - startTime) / 10000000 // 10^-7
//            pdu.add(VariableBinding(SnmpConstants.sysUpTime, TimeTicks(sysUpTime)))
//            pdu.add(VariableBinding(SnmpConstants.snmpTrapOID, SnmpConstants.linkDown))
//
//// payload
//            pdu.add(
//                VariableBinding(
//                    OID("1.3.6.1.2.1.2.2.1.1$downIndex"),
//                    Integer32(downIndex)
//                )
//            )


//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//        return null
//    }

    fun close() {
        try {
            snmp?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun snmpV2cTest(ipHost: String, puerto: Int, comunidad: String, context: Context) {

        val snmpBuilder = SnmpBuilder()
        val snmp = snmpBuilder.udp().v3().threads(2).build()
        snmp.listen()

        val targetAddress: Address = GenericAddress.parse("udp:$ipHost/$puerto")
        val targetBuilder: TargetBuilder<*> = snmpBuilder.target(targetAddress)

        val communityTarget = targetBuilder
            .community(OctetString(comunidad)) // Utilizamos la comunidad para SNMPv2c
            .timeout(500).retries(2)
            .build()

        val pdu = targetBuilder.pdu().type(PDU.GETNEXT).oids("1.3.6.1.2.1.1.2.0").build()
        val snmpRequestFuture = SnmpCompletableFuture.send(snmp, communityTarget, pdu)

        try {
            val vbs: List<VariableBinding> = snmpRequestFuture.get().getAll()
            println("Received: ${snmpRequestFuture.getResponseEvent().response}")
            println("Payload: $vbs")
        } catch (e: Exception) {
            e.printStackTrace()
            println("Request failed: ${e.message}")
        } finally {
            snmp.close()
        }

    }


    fun simpleSnmpV2cTest(ipHost: String, puerto: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Configuramos la conexión SNMP
                val transport: TransportMapping<*> = DefaultUdpTransportMapping()
                val snmp = Snmp(transport)
                snmp.listen()

                // Definimos la dirección del dispositivo SNMP
                val targetAddress: Address = GenericAddress.parse("udp:$ipHost/$puerto")

                // Configuramos el CommunityTarget para SNMPv2c con la comunidad "public"
                val communityTarget = CommunityTarget<Address>().apply {
                    address = targetAddress
                    community = OctetString("public") // Comunidad por defecto
                    version = SnmpConstants.version2c
                    retries = 2
                    timeout = 1000 // Tiempo de espera de 1 segundo
                }

                // Creamos una solicitud PDU para el OID deseado
                val pdu = PDU().apply {
                    type = PDU.GET
                    add(VariableBinding(OID("1.3.6.1.2.1.1.1.0"))) // OID para obtener información del sistema
                }

                // Enviamos la solicitud y obtenemos la respuesta
                val response = snmp.send(pdu, communityTarget)

                // Verificamos si hubo una respuesta del dispositivo
                if (response.response != null) {
                    println("Received response from device: ${response.response.variableBindings}")
                } else {
                    println("No response received, the request timed out.")
                }

                // Cerramos la conexión SNMP
                snmp.close()
            } catch (e: Exception) {
                e.printStackTrace()
                println("Error during SNMP request: ${e.message}")
            }
        }
    }
}