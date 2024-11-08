package com.example.nav_snmp.ui.view.sistema.fragments.procesos_fragment

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nav_snmp.data.model.HostModel
import com.example.nav_snmp.data.model.ProcesosModel
import com.example.nav_snmp.data.repository.HostRepository
import com.example.nav_snmp.utils.CommonOids
import com.example.nav_snmp.utils.Preferencias
import com.example.nav_snmp.utils.SnmpManagerV1
import com.example.nav_snmp.utils.VersionSnmp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProcesosViewModel(
    private val repository: HostRepository,
    private val context: Context
) : ViewModel() {
    private val TAG = "ProcesosViewModel"
    private val _procesoModel = MutableLiveData<List<ProcesosModel>>()
    val procesoModel: MutableLiveData<List<ProcesosModel>>
        get() = _procesoModel

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
                val listaNombre = snmpManagerV1.walk(
                    host,
                    CommonOids.HOST.HR_SWRUN.HR_SWRUN_NAME,
                    context,
                    false
                )

                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        context, "procesos: ${
                            listaNombre.map {
                                "$it   "
                            }
                        }", Toast.LENGTH_SHORT
                    ).show()

                    Log.d(
                        TAG, "procesos: ${
                            listaNombre.map {
                                "$it   "
                            }
                        }"
                    )

                }
                _procesoModel.value = getListaProcesosModel(listaNombre)
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

    //todo terminar
    private fun getListaProcesosModel(
        listaNombres: List<String>,

        ): List<ProcesosModel> {
        val listaProcesosModel = mutableListOf<ProcesosModel>()
        for (i in listaNombres.indices) {
            listaProcesosModel.add(
                ProcesosModel(
                    listaNombres[i],
                    "",
                    "",
                    ""
                )
            )
        }
        return listaProcesosModel
    }

}

class ProcesosViewModelFactory(
    private val repository: HostRepository,
    private val context: android.content.Context
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProcesosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProcesosViewModel(repository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}