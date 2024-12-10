package com.example.nav_snmp.ui.view.tcp.fragments.tabla_de_conexiones_fragment

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nav_snmp.data.model.HostModel
import com.example.nav_snmp.data.model.TablaDeConexionesTCPModel
import com.example.nav_snmp.data.repository.HostRepository
import com.example.nav_snmp.utils.CommonOids
import com.example.nav_snmp.utils.Preferencias
import com.example.nav_snmp.utils.SnmpManagerV1
import com.example.nav_snmp.utils.VersionSnmp

class TablaDeConexionesTCPViewModel(
    private val repository: HostRepository,
    private val context: Context
) : ViewModel() {
    private val TAG = "TablaDeConexionesViewModel"

    private val _tablaDeConexionesTCPModel = MutableLiveData<List<TablaDeConexionesTCPModel>>()
    val tablaDeConexionesTCPModel: MutableLiveData<List<TablaDeConexionesTCPModel>>
        get() = _tablaDeConexionesTCPModel

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
                var estadoActual = snmpManagerV1.walk(
                    host,
                    CommonOids.TCP.TCP_CONN_TABLE.TCP_CONN_STATE,
                    context,
                    false
                )

                /*
                Estos valores representan la unidad de asignación de almacenamiento en bytes,
                es decir, cuántos bytes corresponden a una unidad de almacenamiento.
                Ejemplo :
                Para el primer almacenamiento, la unidad de asignación es 4096 bytes (4 KB)
                asignacion de 65536 bytes (64 KB)
                */
                val direccionIpLocal = snmpManagerV1.walk(
                    host,
                    CommonOids.TCP.TCP_CONN_TABLE.TCP_CONN_LOCAL_ADDRESS,
                    context,
                    false
                )

                val puertoLocal = snmpManagerV1.walk(
                    host,
                    CommonOids.TCP.TCP_CONN_TABLE.TCP_CONN_LOCAL_PORT,
                    context,
                    false
                )

                val direccionIpRemota = snmpManagerV1.walk(
                    host,
                    CommonOids.TCP.TCP_CONN_TABLE.TCP_CONN_REM_ADDRESS,
                    context,
                    false
                )

                val puertoRemoto = snmpManagerV1.walk(
                    host,
                    CommonOids.TCP.TCP_CONN_TABLE.TCP_CONN_REM_PORT,
                    context,
                    false
                )

                estadoActual = agregarDescripcionEstadoActual(estadoActual)

                _tablaDeConexionesTCPModel.value = unirEnUnaSolaLista(
                    estadoActual,
                    direccionIpLocal,
                    puertoLocal,
                    direccionIpRemota,
                    puertoRemoto
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

    private fun unirEnUnaSolaLista(
        estadoActual: List<String>,
        direccionIpLocal: List<String>,
        puertoLocal: List<String>,
        direccionIpRemota: List<String>,
        puertoRemoto: List<String>
    ): List<TablaDeConexionesTCPModel> {
        val list = mutableListOf<TablaDeConexionesTCPModel>()
        for (i in estadoActual.indices) {
            list.add(
                TablaDeConexionesTCPModel(
                    estadoActual[i],
                    direccionIpLocal[i],
                    puertoLocal[i],
                    direccionIpRemota[i],
                    puertoRemoto[i]
                )
            )
        }
        return list
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

class TablaDeConexionesTCPViewModelFactory(
    private val repository: HostRepository,
    private val context: Context
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TablaDeConexionesTCPViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TablaDeConexionesTCPViewModel(repository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
