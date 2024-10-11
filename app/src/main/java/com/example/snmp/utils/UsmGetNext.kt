import org.snmp4j.PDU
import org.snmp4j.fluent.SnmpBuilder
import org.snmp4j.fluent.SnmpCompletableFuture
import org.snmp4j.fluent.TargetBuilder
import org.snmp4j.smi.Address
import org.snmp4j.smi.GenericAddress
import org.snmp4j.smi.VariableBinding

class UsmGetNext {

    suspend fun nextFluent(
        address: String,
        contextName: String,
        securityName: String,
        authPassphrase: String?,
        privPassphrase: String?,
        vararg oids: String
    ) {
        val snmpBuilder = SnmpBuilder()
        val snmp = snmpBuilder.udp().v3().usm().threads(2).build()
        snmp.listen()
        val targetAddress: Address = GenericAddress.parse(address)
        val targetEngineID = snmp.discoverAuthoritativeEngineID(targetAddress, 1000)

        if (targetEngineID != null) {
            val targetBuilder: TargetBuilder<*> = snmpBuilder.target(targetAddress)
            val userTarget = targetBuilder
                .user(securityName, targetEngineID)
                .auth(TargetBuilder.AuthProtocol.hmac192sha256).authPassphrase(authPassphrase)
                .priv(TargetBuilder.PrivProtocol.aes128).privPassphrase(privPassphrase)
                .done()
                .timeout(500).retries(1)
                .build()

            val pdu = targetBuilder.pdu().type(PDU.GETNEXT).oids(*oids).contextName(contextName).build()
            val snmpRequestFuture = SnmpCompletableFuture.send(snmp, userTarget, pdu)

            try {
                val vbs: List<VariableBinding> = snmpRequestFuture.get().getAll()
                println("Received: ${snmpRequestFuture.getResponseEvent().response}")
                println("Payload: $vbs")
            } catch (e: Exception) {
                e.cause?.let {
                    println("Error cause: ${it.message}")
                } ?: run {
                    println("Request failed: ${e.message}")
                }
            }
        } else {
            println("Timeout on engine ID discovery for $targetAddress, GETNEXT not sent.")
        }

        snmp.close()
    }
}