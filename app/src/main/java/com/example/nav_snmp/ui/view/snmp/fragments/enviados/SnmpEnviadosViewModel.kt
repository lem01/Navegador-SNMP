package com.example.nav_snmp.ui.view.snmp.fragments.enviados

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nav_snmp.data.model.HostModel
import com.example.nav_snmp.data.model.SnmpEnviadosModel
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

class SnmpEnviadosViewModel(
    private val repository: HostRepository,
    private val context: Context
) : ViewModel() {
    private val TAG = "SnmpEnviadosViewModel"

    private val _SnmpEnviadosModel = MutableLiveData<SnmpEnviadosModel>()
    val SnmpEnviadosModel: MutableLiveData<SnmpEnviadosModel>
        get() = _SnmpEnviadosModel

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
                var totalPaquetesSnmpEnviados = snmpManagerV1.get(
                    host,
                    CommonOids.SNMP.snmpOutPkts,
                    TipoOperacion.GET,
                    context,
                    false,
                    false
                )

                var solicitudesGetEnviadas = snmpManagerV1.get(
                    host,
                    CommonOids.SNMP.snmpOutGetRequests,
                    TipoOperacion.GET,
                    context,
                    false,
                    false
                )

                var solicitudesGetNextEnviadas = snmpManagerV1.get(
                    host,
                    CommonOids.SNMP.snmpOutGetNexts,
                    TipoOperacion.GET,
                    context,
                    false,
                    false
                )
                var solicitudesSetEnviadas = snmpManagerV1.get(
                    host,
                    CommonOids.SNMP.snmpOutSetRequests,
                    TipoOperacion.GET,
                    context,
                    false,
                    false
                )

                var respuestasGetEnviadas = snmpManagerV1.get(
                    host,
                    CommonOids.SNMP.snmpOutGetResponses,
                    TipoOperacion.GET,
                    context,
                    false,
                    false
                )
                var trapsSnmpEnviados = snmpManagerV1.get(
                    host,
                    CommonOids.SNMP.snmpOutTraps,
                    TipoOperacion.GET,
                    context,
                    false,
                    false
                )

                var respuestaEsDemasiadoGrande = snmpManagerV1.get(
                    host,
                    CommonOids.SNMP.snmpOutTooBigs,
                    TipoOperacion.GET,
                    context,
                    false,
                    false
                )
                var objetoSolicitadoNoExiste = snmpManagerV1.get(
                    host,
                    CommonOids.SNMP.snmpOutNoSuchNames,
                    TipoOperacion.GET,
                    context,
                    false,
                    false
                )

                var invalidosEnOperacionesSet = snmpManagerV1.get(
                    host,
                    CommonOids.SNMP.snmpOutBadValues,
                    TipoOperacion.GET,
                    context,
                    false,
                    false
                )

                var erroresGenericosNoEspecificados = snmpManagerV1.get(
                    host,
                    CommonOids.SNMP.snmpOutGenErrs,
                    TipoOperacion.GET,
                    context,
                    false,
                    false
                )

//                withContext(Dispatchers.Main) {
//                    Log.d("datos: ", "${variablesSolicitudesGetSetRecibidas.map { "dato: $it" }}")
//                }

                _SnmpEnviadosModel.value = SnmpEnviadosModel(
                    totalPaquetesSnmpEnviados,
                    solicitudesGetEnviadas,
                    solicitudesGetNextEnviadas,
                    solicitudesSetEnviadas,
                    respuestasGetEnviadas,
                    trapsSnmpEnviados,
                    respuestaEsDemasiadoGrande,
                    objetoSolicitadoNoExiste,
                    invalidosEnOperacionesSet,
                    erroresGenericosNoEspecificados
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

    class SnmpEnviadosViewModelFactory(
        private val repository: HostRepository,
        private val context: Context
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SnmpEnviadosViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SnmpEnviadosViewModel(repository, context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
