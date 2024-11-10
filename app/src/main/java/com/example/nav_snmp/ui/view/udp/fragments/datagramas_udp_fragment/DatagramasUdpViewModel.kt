package com.example.nav_snmp.ui.view.udp.fragments.datagramas_udp_fragment

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nav_snmp.data.model.DatagramasUdpModel
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

class DatagramasUdpViewModel(
    private val repository: HostRepository,
    private val context: Context
) : ViewModel() {
    private val TAG = "DatagramasUdpViewModel"

    private val _datagramasUdpModel = MutableLiveData<DatagramasUdpModel>()
    val datagramasUdpModel: MutableLiveData<DatagramasUdpModel>
        get() = _datagramasUdpModel

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

                /*
                Estos valores representan la unidad de asignación de almacenamiento en bytes,
                es decir, cuántos bytes corresponden a una unidad de almacenamiento.
                Ejemplo :
                Para el primer almacenamiento, la unidad de asignación es 4096 bytes (4 KB)
                asignacion de 65536 bytes (64 KB)
                */
                val datagramasRecibidos = snmpManagerV1.get(
                    host,
                    CommonOids.UDP.UDP_IN_DATAGRAMS,
                    TipoOperacion.GET,
                    context,
                    false
                )
                val puertosNoDisponibles = snmpManagerV1.get(
                    host,
                    CommonOids.UDP.UDP_NO_PORTS,
                    TipoOperacion.GET,
                    context,
                    false
                )
                val erroresDeEntrada = snmpManagerV1.get(
                    host,
                    CommonOids.UDP.UDP_IN_ERRORS,
                    TipoOperacion.GET,
                    context,
                    false
                )
                val datagramasEnviados = snmpManagerV1.get(
                    host,
                    CommonOids.UDP.UDP_OUT_DATAGRAMS,
                    TipoOperacion.GET,
                    context,
                    false
                )

                _datagramasUdpModel.value =
                    DatagramasUdpModel(
                        datagramasRecibidos,
                        puertosNoDisponibles,
                        erroresDeEntrada,
                        datagramasEnviados
                    )
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
}

class DatagramasUdpViewModelFactory(
    private val repository: HostRepository,
    private val context: Context
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DatagramasUdpViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DatagramasUdpViewModel(repository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
