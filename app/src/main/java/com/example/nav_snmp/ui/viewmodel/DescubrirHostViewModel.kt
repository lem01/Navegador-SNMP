package com.example.nav_snmp.ui.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.collection.mutableIntSetOf
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nav_snmp.data.model.HostModelClass
import com.example.nav_snmp.data.repository.HostRepository
import com.example.nav_snmp.utils.Constantes
import com.example.nav_snmp.utils.SnmpManagerV1
import com.example.nav_snmp.utils.VersionSnmp

class DescubrirHostViewModel(
    private val repository: HostRepository,
    private val context: Context
) : ViewModel() {

    private var preferences: SharedPreferences =
        context.getSharedPreferences("configuracion", android.content.Context.MODE_PRIVATE);
    var editor: SharedPreferences.Editor = preferences.edit();

    companion object {
        lateinit var descubrirHost: DescubrirHostViewModel
    }

    var _hostIntentados = 0

    object hostIntentados {
        var hostIntentados = 0
    }

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> get() = _isLoading

    private val _hostDescubiertos = mutableStateListOf<HostModelClass>()
    val hostDescubiertos: List<HostModelClass> get() = _hostDescubiertos

    private var _hasLoadedHosts = mutableStateOf(false)  // Variable para controlar la carga

    fun setHasLoadedHosts(boolean: Boolean) {
        _hasLoadedHosts.value = boolean  // Actualiza el valor directamente
    }

    suspend fun loadHosts() {
        _isLoading.value = true
        if (_hasLoadedHosts.value) {
            _isLoading.value = false
            return
        }

        try {
            val listHost = buscarHost()
            println("listHost  aaaa ${listHost.size}")

            _hostIntentados = hostIntentados.hostIntentados
            println("host intentados ${_hostIntentados}")

            _hostDescubiertos.clear()
            _hostDescubiertos.addAll(listHost)
            setHasLoadedHosts(true) // Marca como cargados
        } catch (e: Exception) {
            println("Error al cargar hosts: ${e.message}")
            e.printStackTrace()

        } finally {
            _isLoading.value = false  // Siempre finaliza el estado de carga
        }
    }

    fun seleccionarTodos(value: Boolean) {
        _hostDescubiertos.forEach { host ->
            host.checked = value
        }
    }

    private suspend fun buscarHost(): List<HostModelClass> {
        var versionSnmp = preferences.getString(Constantes.SNMP_VERSION, "")
        var host = preferences.getString(Constantes.SNMP_RANGO_IP, "")
        var puerto = preferences.getString(Constantes.SNMP_PUERTO, "")
        var comunidad = preferences.getString(Constantes.SNMP_COMUNIDAD, "")

        var hostModel = HostModelClass(
            id = 0,
            nombreHost = "Host # 1",
            direccionIP = host!!,
            tipoDeDispositivo = "Host",
            versionSNMP = versionSnmp!!,
            puertoSNMP = puerto!!.toInt(),
            comunidadSNMP = comunidad!!,
            estado = true,
            fecha = null,
        )
        if (VersionSnmp.V1.name == versionSnmp) {
            val snmpManagerV1 = SnmpManagerV1()
            return snmpManagerV1.descubrirHost(hostModel, context)
        }

        if (VersionSnmp.V2c.name == versionSnmp) {
            println("Version SNMP V2c")
        }

        return emptyList()
    }

    private fun loadHostDescubiertos(): List<HostModelClass> {
        return List(30) { i ->
            HostModelClass(
                id = i,
                nombreHost = "Host # $i",
                direccionIP = "192.168.1.$i",
                tipoDeDispositivo = "Host",
                versionSNMP = "v1",
                puertoSNMP = 161,
                comunidadSNMP = "public",
                estado = false,
                fecha = null,
                initialChecked = false
            )
        }
    }

    fun changeTaskChecked(item: HostModelClass, checked: Boolean) {
        _hostDescubiertos.find { it.id == item.id }?.let { host ->
            host.checked = checked
        }
    }

}

class DescubrirHostViewModelFactory(
    private val repository: HostRepository,
    private val context: android.content.Context
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DescubrirHostViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DescubrirHostViewModel(repository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
