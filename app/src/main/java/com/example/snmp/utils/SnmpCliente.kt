package com.example.snmp.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import org.snmp4j.CommunityTarget
import org.snmp4j.PDU
import org.snmp4j.PDUv1
import org.snmp4j.ScopedPDU
import org.snmp4j.Snmp
import org.snmp4j.TransportMapping
import org.snmp4j.UserTarget
import org.snmp4j.event.ResponseEvent
import org.snmp4j.mp.SnmpConstants
import org.snmp4j.security.SecurityLevel
import org.snmp4j.smi.Address
import org.snmp4j.smi.GenericAddress
import org.snmp4j.smi.Integer32
import org.snmp4j.smi.OID
import org.snmp4j.smi.OctetString
import org.snmp4j.smi.TimeTicks
import org.snmp4j.smi.VariableBinding
import org.snmp4j.transport.DefaultUdpTransportMapping
import java.util.concurrent.ExecutionException


object SnmpCliente {

    fun userTarget(ipHost: String, puerto: Int, comunidad: String): UserTarget<Address> {
        val target: UserTarget<Address> = UserTarget()
        target.address = GenericAddress.parse("udp:$ipHost/$puerto")
        target.retries = 1

// set timeout to 500 milliseconds: 2*500ms = 1s total timeout
        target.timeout = 500
        target.version = SnmpConstants.version2c
        target.securityLevel = SecurityLevel.AUTH_PRIV
        //community
        target.securityName = OctetString(comunidad)

        return target
    }

    fun communityTarget(
        ipHost: String, puerto: Int, comunidad: String
    ): CommunityTarget<Address> {
        val target: CommunityTarget<Address> = CommunityTarget()
        target.community = OctetString(comunidad)
        target.address = GenericAddress.parse("udp:$ipHost/$puerto")
        target.version = SnmpConstants.version2c
        target.timeout = 1500
        target.retries = 2

        return target

    }

    fun pduGetNextV2(): PDU {
        val pdu = PDU()
        pdu.add(VariableBinding(OID("1.3.6.1.2.1.1.1"))) // sysDescr
        pdu.add(VariableBinding(OID("1.3.6.1.2.1.2.1"))) // ifNumber
        pdu.type = PDU.GETNEXT

        return pdu
    }

    fun pduTrapV2(): PDU {
        val pdu = PDUv1()
        pdu.type = PDU.V1TRAP
        pdu.genericTrap = PDUv1.COLDSTART
        return pdu
    }

    fun pduInform(downIndex: Int, startTime: Long) {
        val pdu = ScopedPDU()
        pdu.type = PDU.INFORM

// sysUpTime
        val sysUpTime: Long = (System.nanoTime() - startTime) / 10000000 // 10^-7
        pdu.add(VariableBinding(SnmpConstants.sysUpTime, TimeTicks(sysUpTime)))
        pdu.add(VariableBinding(SnmpConstants.snmpTrapOID, SnmpConstants.linkDown))

// payload
        pdu.add(
            VariableBinding(
                OID("1.3.6.1.2.1.2.2.1.1$downIndex"), Integer32(downIndex)
            )
        )
    }

//    fun setMensajeSincrono(
//        userTarget: UserTarget<Address>,
//        requestPDU: PDU,
//        context: Context
//    ) {
//        try {
//            val transport: TransportMapping<*> = DefaultUdpTransportMapping()
////            val snmp = Snmp(transport)
////
////            val snmp = Snmp(DefaultUdpTransportMapping())
//
//            val response: ResponseEvent<*> = snmp.send<Address>(requestPDU, userTarget)
//            if (response.response == null) {
//                //reques timeout
//                Toast.makeText(context, "Timeout", Toast.LENGTH_SHORT).show()
//                return
//            }
//            //imprimir en consola
//            val responsePDU: PDU = response.response
//            println("Response PDU: $responsePDU")
//
//            Toast.makeText(context, "Response PDU: $responsePDU", Toast.LENGTH_SHORT).show()
//        } catch (ex: Exception) {
//            System.err.println("Request failed: " + ex.message)
//            Log.e("SnmpLmx", "Request failed: " + ex.message)
//            Toast.makeText(
//                context,
//                "Request failed: " + ex.message,
//                Toast.LENGTH_SHORT
//            ).show()
//        } catch (ex: ExecutionException) {
//            if (ex.cause != null) {
//                System.err.println(ex.cause!!.message)
//                Log.e("ExecutionException", "Request failed: " + ex.message)
//            } else {
//                System.err.println("ExecutionException: " + ex.message)
//                Log.e("ExecutionException", "Request failed: " + ex.message)
//            }
//        } catch (ex: InterruptedException) {
//            if (ex.cause != null) {
//                System.err.println(ex.cause!!.message)
//                Log.e("SnmpLmx", "Request failed: " + ex.message)
//            } else {
//                System.err.println("InterruptedException: " + ex.message)
//                Log.e("SnmpLmx", "InterruptedException: " + ex.message)
//            }
//        } catch (ex: RuntimeException) {
//            System.err.println("RuntimeException: " + ex.message)
//            Log.e("SnmpLmx", "Request failed: " + ex.message)
//            Toast.makeText(
//                context,
//                "Request failed: " + ex.message,
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//
//
//    }
}