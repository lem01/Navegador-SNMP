package com.example.nav_snmp.ui.view.sistema.fragments.informacion_fragment

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nav_snmp.data.model.HostModel
import com.example.nav_snmp.data.repository.HostRepository
import com.example.nav_snmp.utils.CommonOids
import com.example.nav_snmp.utils.Preferencias
import com.example.nav_snmp.utils.SnmpManagerV1
import com.example.nav_snmp.utils.TipoOperacion
import com.example.nav_snmp.utils.VersionSnmp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class InformacionSistemaViewModel(
    private val repository: HostRepository,
    private val context: Context
) : ViewModel() {


    private val _tiempoDeFuncionaiento = MutableLiveData<String>()
    val tiempoDeFuncionaiento: MutableLiveData<String> get() = _tiempoDeFuncionaiento

    private val _showInformacionSistema = MutableLiveData<Boolean>()
    val showInformacionSistema: MutableLiveData<Boolean> get() = _showInformacionSistema

    private val _barraProgreso = MutableLiveData<Boolean>()
    val barraProgreso: MutableLiveData<Boolean> get() = _barraProgreso

    suspend fun cargarDatosSistema(requireContext: Context) {

        barraProgreso.postValue(true)
        _showInformacionSistema.postValue(false)

        val preferences = requireContext.getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val id = preferences.getInt(Preferencias.ID_HOST, 0)
        val host: HostModel = getHostById(id)

        operacionesSnmp(host)

        barraProgreso.postValue(false)
        _showInformacionSistema.postValue(true)

    }

    private suspend fun operacionesSnmp(host: HostModel) {
        if (!isvalidIp(host.direccionIP)) {
            //todo mostrar pagina de error
            return
        }

        when (host.versionSNMP) {
            VersionSnmp.V1.name -> {
                val snmpManagerV1 = SnmpManagerV1()

                _tiempoDeFuncionaiento.value = snmpManagerV1.get(
                    host,
                    CommonOids.SYSTEM.SYS_UP_TIME,
                    TipoOperacion.GET_NEXT,
                    context
                )

                withContext(Dispatchers.Main) {
                    Toast.makeText(context, _tiempoDeFuncionaiento.value, Toast.LENGTH_SHORT)
                        .show()
                }


            }

        }

//        val snmpManagerV2c = SnmpManagerV2c()
//        snmpManagerV2c.get(host, context)
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

class InformacionSistemaViewModelFactory(
    private val repository: HostRepository,
    private val context: android.content.Context
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InformacionSistemaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InformacionSistemaViewModel(repository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}