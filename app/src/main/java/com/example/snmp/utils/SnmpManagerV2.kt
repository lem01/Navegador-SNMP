package com.example.snmp.utils

import CustomProgressDialog
import android.content.Context
import android.widget.Toast
import com.example.snmp.data.model.HostModel
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

class SnmpManagerV2c : SnmpManagerInterface {
    private var snmp: Snmp? = null

    override fun snmpTest(hostModel: HostModel, context: Context) {
        CoroutineScope(Dispatchers.IO).launch {

            lateinit var customProgressDialog: CustomProgressDialog
            CoroutineScope(Dispatchers.Main).launch {
                customProgressDialog =
                    CustomProgressDialog(context, "Conectando", "Conectando con el dispositivo")
                customProgressDialog.show()
           
            }

            delay(1000)

            try {
                val transport: TransportMapping<*> = DefaultUdpTransportMapping()
                snmp = Snmp(transport)
                snmp?.listen()

                val targetAddress: Address =
                    GenericAddress.parse("udp:${hostModel.direccionIP}/${hostModel.puertoSNMP}")

                val communityTarget = CommunityTarget<Address>().apply {
                    address = targetAddress
                    community = OctetString(hostModel.comunidadSNMP)
                    version = SnmpConstants.version2c
                    retries = 2
                    timeout = 1000
                }

                val pdu = PDU().apply {
                    type = PDU.GETNEXT
                    add(VariableBinding(OID("1.3.6.1.2.1.1")))
                }

                val response = snmp?.send(pdu, communityTarget)

                if (response?.response == null)
                    throw Exception("No respuesta recibida, la solicitud agotó el tiempo.")


                if (response.response.errorStatus == PDU.noError) {
                    mensajeToast(context, "Conexión exitosa V2")
                    println("Respuesta: ${response.response.get(0)}")

                    customProgressDialog.dismiss()
                }

                close()
            } catch (e: Exception) {
                customProgressDialog.dismiss()
                e.printStackTrace()
                mensajeToast(context, "Error: ${e.message}")
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

    private suspend fun mensajeToast(context: Context, message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}
