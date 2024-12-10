package com.example.nav_snmp.ui.view.interfaces_de_red

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nav_snmp.data.model.HostModel
import com.example.nav_snmp.data.model.InterfacesDeRedModel
import com.example.nav_snmp.data.repository.HostRepository
import com.example.nav_snmp.utils.CommonOids
import com.example.nav_snmp.utils.Convertidor
import com.example.nav_snmp.utils.Preferencias
import com.example.nav_snmp.utils.SnmpManagerV1
import com.example.nav_snmp.utils.TipoVariableSnmp
import com.example.nav_snmp.utils.VersionSnmp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class InterfacesDeRedViewModel(
    private val repository: HostRepository,
    private val context: Context
) : ViewModel() {
    private val TAG = "TablaDeConexionesViewModel"

    private val _interfacesDeRedModel = MutableLiveData<List<InterfacesDeRedModel>>()
    val interfacesDeRedModel: MutableLiveData<List<InterfacesDeRedModel>>
        get() = _interfacesDeRedModel

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
                var descripcion = snmpManagerV1.walk(
                    host,
                    CommonOids.INTERFACE.IF_DESCR,
                    context,
                    false, false
                )

                var tipo = snmpManagerV1.walk(
                    host,
                    CommonOids.INTERFACE.IF_TYPE,
                    context,
                    false, false
                )

                var velocidad = snmpManagerV1.walk(
                    host,
                    CommonOids.INTERFACE.IF_SPEED,
                    context,
                    false, false
                )
                var direccionMac = snmpManagerV1.walk(
                    host,
                    CommonOids.INTERFACE.IF_PHYS_ADDRESS,
                    context,
                    false, false
                )

                var estadoOperativo = snmpManagerV1.walk(
                    host,
                    CommonOids.INTERFACE.IF_OPER_STATUS,
                    context,
                    false, false
                )
                var estadoAdministrativo = snmpManagerV1.walk(
                    host,
                    CommonOids.INTERFACE.IF_ADMIN_STATUS,
                    context,
                    false, false
                )

                var numeroDeBytesRecividos = snmpManagerV1.walk(
                    host,
                    CommonOids.INTERFACE.IF_IN_OCTETS,
                    context,
                    false, false
                )
                var numeroDeBytesEnviados = snmpManagerV1.walk(
                    host,
                    CommonOids.INTERFACE.IF_OU_OCTETS,
                    context,
                    false, false
                )
                descripcion = descripcion.map {
                    Convertidor.getFormater(it, CommonOids.INTERFACE.IF_DESCR)
                }
                tipo = tipo.map {
                    Convertidor.getFormater(it, CommonOids.INTERFACE.IF_TYPE)
                }

                estadoAdministrativo = estadoAdministrativo.map {
                    Convertidor.getFormater(it, CommonOids.INTERFACE.IF_ADMIN_STATUS)
                }

                estadoOperativo = estadoOperativo.map {
                    Convertidor.getFormater(it, CommonOids.INTERFACE.IF_OPER_STATUS)
                }

                withContext(Dispatchers.Main) {
                    Log.d("descripcion", "${descripcion.map { "dato: $it" }}")
                }

                _interfacesDeRedModel.value = unirEnUnaSolaLista(
                    descripcion,
                    tipo,
                    velocidad,
                    direccionMac,
                    estadoOperativo,
                    estadoAdministrativo,
                    numeroDeBytesRecividos,
                    numeroDeBytesEnviados
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
        descripcion: List<String>,
        tipo: List<String>,
        velocidad: List<String>,
        direccionMac: List<String>,
        estadoOperativo: List<String>,
        estadoAdministrativo: List<String>,
        numeroDeBytesRecividos: List<String>,
        numeroDeBytesEnviados: List<String>
    ): List<InterfacesDeRedModel> {
        val list = mutableListOf<InterfacesDeRedModel>()
        for (i in descripcion.indices) {
            list.add(
                InterfacesDeRedModel(
                    descripcion[i],
                    tipo[i],
                    velocidad[i],
                    direccionMac[i],
                    estadoOperativo[i],
                    estadoAdministrativo[i],
                    numeroDeBytesRecividos[i],
                    numeroDeBytesEnviados[i]
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

    suspend fun setEstadoAdministrativo(value: Int, position: Int) {
        val preferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val id = preferences.getInt(Preferencias.ID_HOST, 0)
        val host: HostModel = getHostById(id)
        val snmpManagerV1 = SnmpManagerV1()

        val oid = CommonOids.INTERFACE.IF_ADMIN_STATUS + ".${position + 1}"

        var result = snmpManagerV1.set(
            host,
            oid,
            value,
            TipoVariableSnmp.INTEGER,  //INTEGER32, OCTETSTRING, TIMETICKS, IPADDRESS, COUNTER32, GAUGE32, COUNTER64, OID
            context
        )

        result =
            if (result.toString() == "1") "Up (1) " else if (result.toString() == "2") "Down (2)"
            else if (result.toString() == "3") "Testing (3)"
            else ""

        if (result == "") return


        _interfacesDeRedModel.value?.let {
            val updatedItem = it[position].copy(estadoAdministrativo = result.toString())
            _interfacesDeRedModel.value =
                it.toMutableList().apply { this[position] = updatedItem }
        }
        Log.d(TAG, "setEstadoAdministrativo: $result")
//        return item.copy(estadoAdministrativo = result.toString())

    }

    fun setEstadoAdministrativo2(value: Int, adapterPosition: Int) {

        _interfacesDeRedModel.value?.let {
            val updatedItem = it[adapterPosition].copy(estadoAdministrativo = value.toString())
            _interfacesDeRedModel.value =
                it.toMutableList().apply { this[adapterPosition] = updatedItem }
        }
    }


    class InterfacesDeRedViewModelFactory(
        private val repository: HostRepository,
        private val context: Context
    ) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(InterfacesDeRedViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return InterfacesDeRedViewModel(repository, context) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}