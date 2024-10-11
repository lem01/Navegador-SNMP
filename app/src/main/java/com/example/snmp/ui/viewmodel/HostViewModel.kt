package com.example.snmp.ui.viewmodel

import UsmGetNext
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.snmp.data.model.HostModel
import com.example.snmp.data.repository.HostRepository
import com.example.snmp.utils.SnmpManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.snmp4j.CommunityTarget
import org.snmp4j.smi.Address
import org.snmp4j.smi.GenericAddress
import org.snmp4j.smi.OctetString


class HostViewModel(private val repository: HostRepository) : ViewModel() {

    val allHosts = repository.getAllHosts().asLiveData()

    fun addHost(host: HostModel) {
        viewModelScope.launch {
            repository.saveHost(host)
        }
    }


    fun snmpV1Test(ipHost: String, puerto: Int, comunidad: String) {
        if (isvalidIp(ipHost)) {
            val target: CommunityTarget<*> = CommunityTarget<Address>()
            target.community = OctetString(comunidad)
            target.address = GenericAddress.parse(ipHost) as Nothing?

        }

    }

    fun snmpV2cTest(ipHost: String, puerto: Int, comunidad: String, context: Context) {
        if (isvalidIp(ipHost)) {
            CoroutineScope(Dispatchers.IO).launch {
                val snmpManager = SnmpManager()
//                snmpManager.snmpV2cTest("192.168.1.1", puerto, comunidad, context)
                snmpManager.simpleSnmpV2cTest(ipHost, puerto)

            }
        }

//        if (isvalidIp(ipHost)) {
//            val usmGetNext = UsmGetNext()
//            val targetAddress = GenericAddress.parse("udp:$ipHost/$puerto")
//            val contextName = "public"
//            val securityName = "noAuthNoPriv"
//            val authPassphrase = null
//            val oids = arrayOf("1.3.6.1.2.1.1.1.0")
//            val usmGetNextKotlin = UsmGetNext()
//
//            try {
//                UsmGetNext.nextFluent(targetAddress, context, securityName, authPassphrase, authPassphrase, *oids)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//            CoroutineScope(Dispatchers.IO).launch {
////                val snmpManager = SnmpManager(ipHost, comunidad, puerto)
////
////                val response = snmpManager.getAsString("1.3.6.1.2.1.1.1.0")
////                withContext(Dispatchers.Main) {
////                    Toast.makeText(
////                        context,
////                        response ?: "No se recibió respuesta",
////                        Toast.LENGTH_SHORT
////                    ).show()
////                }
//            }
    }

}
//        val communityTarget = SnmpCliente.communityTarget(ipHost, puerto, comunidad)
//        val userTarget = SnmpCliente.userTarget(ipHost, puerto, comunidad)
//
//        val pdu = SnmpCliente.pduGetNextV2()
//
//        println("PDU: $pdu")
//        println("CommunityTarget: $communityTarget")
//        println("UserTarget: $userTarget")
//
//        SnmpCliente.setMensajeSincrono(userTarget, pdu, context)


//}


private fun isvalidIp(input: String): Boolean {
    // Expresión regular para IPv4
    val ipv4Pattern = Regex(
        "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"
    )
    // Expresión regular para IPv6
    val ipv6Pattern = Regex(
        "^(?:[a-fA-F0-9]{1,4}:){7}[a-fA-F0-9]{1,4}$"

    )

    return ipv4Pattern.matches(input) || ipv6Pattern.matches(input)
}
