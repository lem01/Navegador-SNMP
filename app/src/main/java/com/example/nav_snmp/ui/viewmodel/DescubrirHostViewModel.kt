package com.example.nav_snmp.ui.viewmodel

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nav_snmp.data.model.HostModel
import com.example.nav_snmp.data.model.HostModelClass
import com.example.nav_snmp.data.repository.HostRepository
import com.example.nav_snmp.utils.Constantes
import com.example.nav_snmp.utils.SnmpManagerV1
import com.example.nav_snmp.utils.VersionSnmp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DescubrirHostViewModel(
    private val repository: HostRepository,
    private val context: android.content.Context
) : ViewModel() {

    private var preferences: SharedPreferences =
        context.getSharedPreferences("configuracion", android.content.Context.MODE_PRIVATE);
    var editor: SharedPreferences.Editor = preferences.edit();

    companion object {
        lateinit var descubrirHost: DescubrirHostViewModel
    }

    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    private val _hostDescubiertos = mutableStateListOf<HostModelClass>()
    val hostDescubiertos: List<HostModelClass> get() = _hostDescubiertos

    private var hasLoadedHosts = false // Variable para controlar la carga

    suspend fun loadHosts() {

//        println("cargando host ${preferences.getString(Constantes.SNMP_RANGO_IP, "")}")



        if (hasLoadedHosts) return

//        viewModelScope.launch {
//            _isLoading.value = true
//            // Simula carga de datos
//            delay(1500)
//            val loadedHosts = loadHostDescubiertos()
//            _hostDescubiertos.addAll(loadedHosts)
//            hasLoadedHosts = true
//            _isLoading.value = false
//        }
//
        _isLoading.value = true
        val listHost = buscarHost()
        println("listHost $listHost")
        hasLoadedHosts = true
        _isLoading.value = false
    }

    private suspend fun buscarHost(): List<HostModel> {
        var versionSnmp = preferences.getString(Constantes.SNMP_VERSION, "")
        var host = preferences.getString(Constantes.SNMP_RANGO_IP, "")
        var puerto = preferences.getString(Constantes.SNMP_PUERTO, "")
        var comunidad = preferences.getString(Constantes.SNMP_COMUNIDAD, "")

        var hostModel = HostModel(
            id = 1,
            nombreHost = "Host # 1",
            direccionIP = host!!,
            tipoDeDispositivo = "Host",
            versionSNMP = versionSnmp!!,
            puertoSNMP = puerto!!.toInt(),
            comunidadSNMP = comunidad!!,
            estado = false,
            fecha = null,
        )
        println("host model $hostModel")
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
