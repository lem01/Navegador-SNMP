package com.example.nav_snmp.ui.view.ip.fragments.datos_ip

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nav_snmp.data.model.DatosIpModel
import com.example.nav_snmp.data.model.HostModel
import com.example.nav_snmp.data.model.IcmpEntradaModel
import com.example.nav_snmp.data.repository.HostRepository
import com.example.nav_snmp.utils.CommonOids
import com.example.nav_snmp.utils.Preferencias
import com.example.nav_snmp.utils.SnmpManagerV1
import com.example.nav_snmp.utils.TipoOperacion
import com.example.nav_snmp.utils.TipoVariableSnmp
import com.example.nav_snmp.utils.VersionSnmp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class DatosIpViewModel(
    private val repository: HostRepository,
    private val context: Context
) : ViewModel() {
    private val TAG = "DatosIpViewModel"

    private val _datosIpModel = MutableLiveData<DatosIpModel>()
    val datosIpModel: MutableLiveData<DatosIpModel>
        get() = _datosIpModel

    private val _showDatos = MutableLiveData<Boolean>()
    val showDatos: MutableLiveData<Boolean> get() = _showDatos

    private val _barraProgreso = MutableLiveData<Boolean>()
    val barraProgreso: MutableLiveData<Boolean> get() = _barraProgreso

    suspend fun cargarDatos(requireContext: Context) {

        barraProgreso.postValue(true)
        _showDatos.postValue(false)

        val preferences = requireContext.getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val id = preferences.getInt(Preferencias.ID_HOST, 0)
        val host: HostModel = getHostById(id)

        operacionesSnmp(host)

        barraProgreso.postValue(false)
        _showDatos.postValue(true)

    }

    private suspend fun operacionesSnmp(host: HostModel) {
        if (!isvalidIp(host.direccionIP)) {
            //todo mostrar pagina de error
            return
        }


        when (host.versionSNMP) {
            VersionSnmp.V1.name -> {
                val snmpManagerV1 = SnmpManagerV1()

                var estadoReenvioPaquetes = snmpManagerV1.get(
                    host,
                    CommonOids.IP.ipForwarding,
                    TipoOperacion.GET,
                    context,
                    false,
                    showMensajeAdvertencia = false
                )
                var ttlPredeterminado = snmpManagerV1.get(
                    host,
                    CommonOids.IP.ipDefaultTTL,
                    TipoOperacion.GET,
                    context,
                    false,
                    showMensajeAdvertencia = false
                )
                var totalDatagramasIpRecibidos = snmpManagerV1.get(
                    host,
                    CommonOids.IP.ipInReceives,
                    TipoOperacion.GET,
                    context,
                    false,
                    showMensajeAdvertencia = false
                )
                var erroresCabecera = snmpManagerV1.get(
                    host,
                    CommonOids.IP.ipInHdrErrors,
                    TipoOperacion.GET,
                    context,
                    false,
                    showMensajeAdvertencia = false
                )
                var erroresDireccion = snmpManagerV1.get(
                    host,
                    CommonOids.IP.ipInAddrErrors,
                    TipoOperacion.GET,
                    context,
                    false,
                    showMensajeAdvertencia = false
                )
                var datagramasIpReenviados = snmpManagerV1.get(
                    host,
                    CommonOids.IP.ipForwDatagrams,
                    TipoOperacion.GET,
                    context,
                    false,
                    showMensajeAdvertencia = false
                )
                var datagranasConProtcolosDesconocidos = snmpManagerV1.get(
                    host,
                    CommonOids.IP.ipInUnknownProtos,
                    TipoOperacion.GET,
                    context,
                    false,
                    showMensajeAdvertencia = false
                )
                var descartadosSinError = snmpManagerV1.get(
                    host,
                    CommonOids.IP.ipInDiscards,
                    TipoOperacion.GET,
                    context,
                    false,
                    showMensajeAdvertencia = false
                )
                var entregadosCorrectamente = snmpManagerV1.get(
                    host,
                    CommonOids.IP.ipInDelivers,
                    TipoOperacion.GET,
                    context,
                    false,
                    showMensajeAdvertencia = false
                )
                var generadosParaEnvio = snmpManagerV1.get(
                    host,
                    CommonOids.IP.ipOutRequests,
                    TipoOperacion.GET,
                    context,
                    false,
                    showMensajeAdvertencia = false
                )
                var descartadosAlEnviar = snmpManagerV1.get(
                    host,
                    CommonOids.IP.ipOutDiscards,
                    TipoOperacion.GET,
                    context,
                    false,
                    showMensajeAdvertencia = false
                )
                var sinRutaDisponible = snmpManagerV1.get(
                    host,
                    CommonOids.IP.ipOutNoRoutes,
                    TipoOperacion.GET,
                    context,
                    false,
                    showMensajeAdvertencia = false
                )
                var tiempoDesEsperaReensamblajeDeFragmentos = snmpManagerV1.get(
                    host,
                    CommonOids.IP.ipReasmTimeout,
                    TipoOperacion.GET,
                    context,
                    false,
                    showMensajeAdvertencia = false
                )
                var solicitudesReensambleajeDeFragmentos = snmpManagerV1.get(
                    host,
                    CommonOids.IP.ipReasmReqds,
                    TipoOperacion.GET,
                    context,
                    false,
                    showMensajeAdvertencia = false
                )
                var reensamblajeExitosos = snmpManagerV1.get(
                    host,
                    CommonOids.IP.ipReasmOKs,
                    TipoOperacion.GET,
                    context,
                    false,
                    showMensajeAdvertencia = false
                )
                var fallosEnElReensamblajeDatagramasIP = snmpManagerV1.get(
                    host,
                    CommonOids.IP.ipReasmFails,
                    TipoOperacion.GET,
                    context,
                    false,
                    showMensajeAdvertencia = false
                )
                var fragmentacionExitosaDatagramasIp = snmpManagerV1.get(
                    host,
                    CommonOids.IP.ipFragOKs,
                    TipoOperacion.GET,
                    context,
                    false,
                    showMensajeAdvertencia = false
                )
                var fallosEnlaFragmentacionDeDatagramasIp = snmpManagerV1.get(
                    host,
                    CommonOids.IP.ipFragFails,
                    TipoOperacion.GET,
                    context,
                    false,
                    showMensajeAdvertencia = false
                )
                var fragmentosGeneradosPorFragmentacion = snmpManagerV1.get(
                    host,
                    CommonOids.IP.ipFragCreates,
                    TipoOperacion.GET,
                    context,
                    false,
                    showMensajeAdvertencia = false
                )
                var entradasEnrutamientoDescartadas = snmpManagerV1.get(
                    host,
                    CommonOids.IP.ipRoutingDiscards,
                    TipoOperacion.GET,
                    context,
                    false,
                    showMensajeAdvertencia = false
                )


                estadoReenvioPaquetes = formatEstadoReenvioDePaquetes(estadoReenvioPaquetes)
//                tiempoMaximoDeReTransmision = validateDatosIp(tiempoMaximoDeReTransmision)
//                maximoDeConexiones = validateDatosIp(maximoDeConexiones)

                _datosIpModel.value = DatosIpModel(
                    estadoReenvioPaquetes,
                    ttlPredeterminado,
                    totalDatagramasIpRecibidos,
                    erroresCabecera,
                    erroresDireccion,
                    datagramasIpReenviados,
                    datagranasConProtcolosDesconocidos,
                    descartadosSinError,
                    entregadosCorrectamente,
                    generadosParaEnvio,
                    descartadosAlEnviar,
                    sinRutaDisponible,
                    tiempoDesEsperaReensamblajeDeFragmentos,
                    solicitudesReensambleajeDeFragmentos,
                    reensamblajeExitosos,
                    fallosEnElReensamblajeDatagramasIP,
                    fragmentacionExitosaDatagramasIp,
                    fallosEnlaFragmentacionDeDatagramasIp,
                    fragmentosGeneradosPorFragmentacion,
                    entradasEnrutamientoDescartadas,
                )

            }
        }
    }

    private fun formatEstadoReenvioDePaquetes(estadoReenvioPaquetes: String): String {
        return if (estadoReenvioPaquetes == "1") "Habilitado(1)" else if (estadoReenvioPaquetes == "2") "Deshabilitado(2)" else ""
    }

    private fun validateDatosIp(s: String) = if (s == "-1") "Sin limite" else s

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


    suspend fun getHostById(idHost: Int): HostModel {
        return repository.getHostById(idHost)
    }

    suspend fun editarDatos(oid: String, valorNuevo: String) {

        when (oid) {
            CommonOids.IP.ipForwarding -> {
                if (!esNumeroEnteroValido(valorNuevo))
                    return

                var result = operacionSet(oid, valorNuevo.toInt(), TipoVariableSnmp.INTEGER)
                result = formatEstadoReenvioDePaquetes(result)

                _datosIpModel.value = _datosIpModel.value?.copy(estadoReenvioPaquetes = result)
            }

            CommonOids.IP.ipDefaultTTL -> {
                if (!esNumeroEnteroValido(valorNuevo))
                    return

                val result = operacionSet(oid, valorNuevo.toInt(), TipoVariableSnmp.INTEGER)
                _datosIpModel.value = _datosIpModel.value?.copy(ttlPredeterminado = result)
            }

        }
    }

    fun esNumeroEnteroValido(valor: String): Boolean {
        return try {
            valor.toInt()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }


    private suspend fun operacionSet(
        oid: String,
        valorNuevo: Any,
        tipoVariableSnmp: String
    ): String {
        val preferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val id = preferences.getInt(Preferencias.ID_HOST, 0)
        val host: HostModel = getHostById(id)
        val snmpManagerV1 = SnmpManagerV1()

        val result = snmpManagerV1.set(
            host,
            oid,
            valorNuevo,
            tipoVariableSnmp,  //INTEGER32, OCTETSTRING, TIMETICKS, IPADDRESS, COUNTER32, GAUGE32, COUNTER64, OID
            context
        )

        Log.d("InformacionSistemaViewModel", "operacionSet: $result")
        return result as String
    }
}

class DatosIpViewModelFactory(
    private val repository: HostRepository,
    private val context: Context
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DatosIpViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DatosIpViewModel(repository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
