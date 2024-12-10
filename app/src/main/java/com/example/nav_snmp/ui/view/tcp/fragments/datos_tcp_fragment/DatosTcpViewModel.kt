package com.example.nav_snmp.ui.view.tcp.fragments.datos_tcp_fragment

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nav_snmp.data.model.DatosTcpModel
import com.example.nav_snmp.data.model.HostModel
import com.example.nav_snmp.data.model.IcmpEntradaModel
import com.example.nav_snmp.data.repository.HostRepository
import com.example.nav_snmp.utils.CommonOids
import com.example.nav_snmp.utils.Preferencias
import com.example.nav_snmp.utils.SnmpManagerV1
import com.example.nav_snmp.utils.TipoOperacion
import com.example.nav_snmp.utils.VersionSnmp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class DatosTcpViewModel(
    private val repository: HostRepository,
    private val context: Context
) : ViewModel() {
    private val TAG = "DatosTcpViewModel"

    private val _datosTcpModel = MutableLiveData<DatosTcpModel>()
    val datosTcpModel: MutableLiveData<DatosTcpModel>
        get() = _datosTcpModel

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

                var tiempoMinimoDeReTransmision = snmpManagerV1.get(
                    host,
                    CommonOids.TCP.TCP_RTO_MIN,
                    TipoOperacion.GET,
                    context,
                    false,
                    showMensajeAdvertencia = false
                )

                var tiempoMaximoDeReTransmision = snmpManagerV1.get(
                    host,
                    CommonOids.TCP.TCP_RTO_MAX,
                    TipoOperacion.GET,
                    context,
                    false,
                    showMensajeAdvertencia = false
                )
                var maximoDeConexiones = snmpManagerV1.get(
                    host,
                    CommonOids.TCP.TCP_MAX_CONN,
                    TipoOperacion.GET,
                    context,
                    false,
                    showMensajeAdvertencia = false
                )


                ////////////////

                var conexionesActivasIniciadas = snmpManagerV1.get(
                    host,
                    CommonOids.TCP.TCP_ACTIVE_OPENS,
                    TipoOperacion.GET,
                    context,
                    false,
                    showMensajeAdvertencia = false
                )

                var conexionesPasivasEstablecidas =
                    snmpManagerV1.get( //solicitudes de conexion puede ser
                        host,
                        CommonOids.TCP.TCP_PASSIVE_OPENS,
                        TipoOperacion.GET,
                        context,
                        false,
                        showMensajeAdvertencia = false
                    )

                var intentosDeConexionesFallidos = snmpManagerV1.get(  //numero de intentos fallidos
                    host,
                    CommonOids.TCP.TCP_ATTEMPT_FAILS,
                    TipoOperacion.GET,
                    context,
                    false,
                    showMensajeAdvertencia = false
                )

                var reinciosDeConexiones = snmpManagerV1.get(  //numero de intentos fallidos
                    host,
                    CommonOids.TCP.TCP_ESTAB_RESETS,
                    TipoOperacion.GET,
                    context,
                    false,
                    showMensajeAdvertencia = false
                )

                ////////

                var conexionesEstablecidasActuales = snmpManagerV1.get(
                    host,
                    CommonOids.TCP.TCP_CURR_ESTAB,
                    TipoOperacion.GET,
                    context,
                    false,
                    showMensajeAdvertencia = false
                )

                var segmentosRecividios = snmpManagerV1.get(
                    host,
                    CommonOids.TCP.TCP_IN_SEGS,
                    TipoOperacion.GET,
                    context,
                    false,
                    showMensajeAdvertencia = false
                )

                var segmentosEnviados = snmpManagerV1.get(
                    host,
                    CommonOids.TCP.TCP_OUT_SEGS,
                    TipoOperacion.GET,
                    context,
                    false,
                    showMensajeAdvertencia = false
                )


                var segmentosReTransmitidos = snmpManagerV1.get(
                    host,
                    CommonOids.TCP.TCP_RETRANS_SEGS,
                    TipoOperacion.GET,
                    context,
                    false,
                    showMensajeAdvertencia = false
                )


                tiempoMinimoDeReTransmision = validateDatosTcp(tiempoMinimoDeReTransmision)
                tiempoMaximoDeReTransmision = validateDatosTcp(tiempoMaximoDeReTransmision)
                maximoDeConexiones = validateDatosTcp(maximoDeConexiones)

                _datosTcpModel.value = DatosTcpModel(
                    tiempoMinimoDeReTransmision,
                    tiempoMaximoDeReTransmision,
                    maximoDeConexiones,
                    conexionesActivasIniciadas,
                    conexionesPasivasEstablecidas,
                    intentosDeConexionesFallidos,
                    reinciosDeConexiones,
                    conexionesEstablecidasActuales,
                    segmentosRecividios,
                    segmentosEnviados,
                    segmentosReTransmitidos,
                )

            }
        }
    }

    private fun validateDatosTcp(s: String) = if (s == "-1") "Sin limite" else s

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
}

class DatosTcpViewModelFactory(
    private val repository: HostRepository,
    private val context: Context
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DatosTcpViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DatosTcpViewModel(repository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
