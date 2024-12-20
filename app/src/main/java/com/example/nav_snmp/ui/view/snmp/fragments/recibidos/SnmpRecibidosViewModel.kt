package com.example.nav_snmp.ui.view.snmp.fragments.recibidos

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nav_snmp.data.model.HostModel
import com.example.nav_snmp.data.model.SnmpRecibidosModel
import com.example.nav_snmp.data.repository.HostRepository
import com.example.nav_snmp.utils.CommonOids
import com.example.nav_snmp.utils.Convertidor
import com.example.nav_snmp.utils.Preferencias
import com.example.nav_snmp.utils.SnmpManagerV1
import com.example.nav_snmp.utils.TipoOperacion
import com.example.nav_snmp.utils.TipoVariableSnmp
import com.example.nav_snmp.utils.VersionSnmp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SnmpRecibidosViewModel(
    private val repository: HostRepository,
    private val context: Context
) : ViewModel() {
    private val TAG = "SnmpRecibidosViewModel"

    private val _snmpRecibidosModel = MutableLiveData<SnmpRecibidosModel>()
    val snmpRecibidosModel: MutableLiveData<SnmpRecibidosModel>
        get() = _snmpRecibidosModel

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
                var variablesSolicitudesGetSetRecibidas = snmpManagerV1.get(
                    host,
                    CommonOids.SNMP.snmpInTotalReqVars,
                    TipoOperacion.GET,
                    context,
                    false,
                    false
                )

                var variablesSolicitudesSetRecibidas = snmpManagerV1.get(
                    host,
                    CommonOids.SNMP.snmpInTotalSetVars,
                    TipoOperacion.GET,
                    context,
                    false,
                    false
                )

                var solicitudesGetRecibidas = snmpManagerV1.get(
                    host,
                    CommonOids.SNMP.snmpInGetRequests,
                    TipoOperacion.GET,
                    context,
                    false,
                    false
                )
                var solicitudesGetNextRecibidas = snmpManagerV1.get(
                    host,
                    CommonOids.SNMP.snmpInGetNexts,
                    TipoOperacion.GET,
                    context,
                    false,
                    false
                )

                var solicitudesSetRecibidas = snmpManagerV1.get(
                    host,
                    CommonOids.SNMP.snmpInSetRequests,
                    TipoOperacion.GET,
                    context,
                    false,
                    false
                )
                var respuestasGetRecibidas = snmpManagerV1.get(
                    host,
                    CommonOids.SNMP.snmpInGetResponses,
                    TipoOperacion.GET,
                    context,
                    false,
                    false
                )

                var mensajesTrapSnmpRecibidos = snmpManagerV1.get(
                    host,
                    CommonOids.SNMP.snmpInTraps,
                    TipoOperacion.GET,
                    context,
                    false,
                    false
                )
                var conVersionSnmpNoSoportada = snmpManagerV1.get(
                    host,
                    CommonOids.SNMP.snmpInBadVersions,
                    TipoOperacion.GET,
                    context,
                    false,
                    false
                )


                var nombreComunidadSnmpInvalido = snmpManagerV1.get(
                    host,
                    CommonOids.SNMP.snmpInBadCommunityNames,
                    TipoOperacion.GET,
                    context,
                    false,
                    false
                )
//////////////////
                var conUsoInapropiadoDeComunidad = snmpManagerV1.get(
                    host,
                    CommonOids.SNMP.snmpInBadCommunityUses,
                    TipoOperacion.GET,
                    context,
                    false,
                    false
                )
                var erroresDeSintaxisAsn1 = snmpManagerV1.get(
                    host,
                    CommonOids.SNMP.snmpInASNParseErrs,
                    TipoOperacion.GET,
                    context,
                    false,
                    false
                )
                var grandeParaCaberEnUnMensaje = snmpManagerV1.get(
                    host,
                    CommonOids.SNMP.snmpInTooBigs,
                    TipoOperacion.GET,
                    context,
                    false,
                    false
                )
                var objetoSolicitadoNoExiste = snmpManagerV1.get(
                    host,
                    CommonOids.SNMP.snmpInNoSuchNames,
                    TipoOperacion.GET,
                    context,
                    false,
                    false
                )
                var solicitudesDeEscrituraAObjetosDeLectura = snmpManagerV1.get(
                    host,
                    CommonOids.SNMP.snmpInReadOnlys,
                    TipoOperacion.GET,
                    context,
                    false,
                    false
                )

                var erroresGenericosNoEspecificados = snmpManagerV1.get(
                    host,
                    CommonOids.SNMP.snmpInGenErrs,
                    TipoOperacion.GET,
                    context,
                    false,
                    false
                )

                withContext(Dispatchers.Main) {
                    Log.d("datos: ", "${variablesSolicitudesGetSetRecibidas.map { "dato: $it" }}")
                }

                _snmpRecibidosModel.value = SnmpRecibidosModel(
                    variablesSolicitudesGetSetRecibidas,
                    variablesSolicitudesSetRecibidas.toString(),
                    solicitudesGetRecibidas.toString(),
                    solicitudesGetNextRecibidas.toString(),
                    solicitudesSetRecibidas.toString(),
                    respuestasGetRecibidas.toString(),
                    mensajesTrapSnmpRecibidos.toString(),
                    conVersionSnmpNoSoportada.toString(),
                    nombreComunidadSnmpInvalido.toString(),
                    conUsoInapropiadoDeComunidad.toString(),
                    erroresDeSintaxisAsn1.toString(),
                    grandeParaCaberEnUnMensaje.toString(),
                    objetoSolicitadoNoExiste.toString(),
                    solicitudesDeEscrituraAObjetosDeLectura.toString(),
                    erroresGenericosNoEspecificados.toString()
                )
            }

        }
    }

    private fun agregarDescripcionEstadoActual(estadoActual: List<String>): List<String> {
        return estadoActual.map {
            when (it) {
                "1" -> "Cerrada($it)"
                "2" -> "Escuchando($it)"
                "3" -> "Conexión enviada($it)"
                "4" -> "Conexión recibida($it)"
                "5" -> "Conexión Establecida($it)"
                "6" -> "Esperando fin 1($it)"
                "7" -> "Esperando fin 2($it)"
                "8" -> "Esperando cierre($it)"
                "9" -> "Ultimo ACK($it)"
                "10" -> "Cerrando($it)"
                "11" -> "Esperando conexión($it)"
                else -> "Desconocido($it)"
            }
        }
    }

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

    class SnmpRecibidosViewModelFactory(
        private val repository: HostRepository,
        private val context: Context
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SnmpRecibidosViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SnmpRecibidosViewModel(repository, context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
