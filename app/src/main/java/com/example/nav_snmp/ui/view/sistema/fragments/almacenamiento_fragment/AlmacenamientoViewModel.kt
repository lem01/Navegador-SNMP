package com.example.nav_snmp.ui.view.sistema.fragments.almacenamiento_fragment

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nav_snmp.data.model.AlmacenamientoModel
import com.example.nav_snmp.data.model.HostModel
import com.example.nav_snmp.data.repository.HostRepository
import com.example.nav_snmp.utils.CommonOids
import com.example.nav_snmp.utils.Convertidor
import com.example.nav_snmp.utils.Preferencias
import com.example.nav_snmp.utils.SnmpManagerV1
import com.example.nav_snmp.utils.VersionSnmp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AlmacenamientoViewModel(
    private val repository: HostRepository,
    private val context: Context
) : ViewModel() {
    private val TAG = "AlmacenamientoViewModel"

    private val _almacenamientoModel = MutableLiveData<List<AlmacenamientoModel>>()
    val almacenamientoModel: MutableLiveData<List<AlmacenamientoModel>>
        get() = _almacenamientoModel

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
                val listaNombres = snmpManagerV1.walk(
                    host,
                    CommonOids.HOST.HRSTORAGE.HR_STORAGE_DESCR,
                    context,
                    false,
                    false
                )

                /*
                Estos valores representan la unidad de asignación de almacenamiento en bytes,
                es decir, cuántos bytes corresponden a una unidad de almacenamiento.
                Ejemplo :
                Para el primer almacenamiento, la unidad de asignación es 4096 bytes (4 KB)
                asignacion de 65536 bytes (64 KB)
                */
                val listaUnidadesDeAsignacion = snmpManagerV1.walk(
                    host,
                    CommonOids.HOST.HRSTORAGE.HR_STORAGE_ALLOCATION_UNITS,
                    context,
                    false,
                    false
                )

                val listaTamanios = snmpManagerV1.walk(
                    host,
                    CommonOids.HOST.HRSTORAGE.HR_STORAGE_SIZE,
                    context,
                    false,
                    false
                )

                val listaAlmacenamientoUsado = snmpManagerV1.walk(
                    host,
                    CommonOids.HOST.HRSTORAGE.HR_STORAGE_USED,
                    context,
                    false,
                    false
                )

                var tamaniosEnGigabytes: List<Double> =
                    convertToGigabytes(listaUnidadesDeAsignacion, listaTamanios)

                val almacenamientoUsadoEnGigabytes: List<Double> =
                    convertToGigabytes(listaUnidadesDeAsignacion, listaAlmacenamientoUsado)

                val listAlmacenamientoLibreGytes: List<Double> =
                    getAlmacenaminentoLibre(tamaniosEnGigabytes, almacenamientoUsadoEnGigabytes)

                var stringTamaniosEnGigabytes =
                    Convertidor.formatDoubleToString(tamaniosEnGigabytes).map { "$it GB" }
                var stringAlmacenamientoUsadoEnGigabytes =
                    Convertidor.formatDoubleToString(almacenamientoUsadoEnGigabytes)
                        .map { "$it GB" }
                var stringListAlmacenamientoLibreGytes =
                    Convertidor.formatDoubleToString(listAlmacenamientoLibreGytes).map { "$it GB" }

//                withContext(Dispatchers.Main) {
//                    Toast.makeText(
//                        context,
//                        tamaniosEnGigabytes.toString(),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    Log.d(
//                        TAG,
//                        "datos: $tamaniosEnGigabytes usado: $almacenamientoUsadoEnGigabytes libre: $listAlmacenamientoLibreGytes"
//                    )
//                }

                val listaAlmacenamientoModel = getListaAlmacenamientoModel(
                    listaNombres,
                    stringTamaniosEnGigabytes,
                    stringAlmacenamientoUsadoEnGigabytes,
                    stringListAlmacenamientoLibreGytes
                )

                withContext(Dispatchers.Main) {
//                    Toast.makeText(
//                        context,
//                        tamaniosEnGigabytes.toString(),
//                        Toast.LENGTH_SHORT
//                    ).show()
                    Log.d(TAG, "datosssss: $stringTamaniosEnGigabytes")
                    Log.d(
                        "datoslista: ${
                            listaAlmacenamientoModel.map {
                                "nombre: ${it.nombre}, tamaño: ${it.tamaño}, usado: ${it.usado}, libre: ${it.libre}      "
                            }
                        }",
                        TAG,
                    )
                }

                _almacenamientoModel.value = listaAlmacenamientoModel

            }

        }
    }

    private fun getListaAlmacenamientoModel(
        listaNombres: List<String>,
        tamaniosEnGigabytes: List<String>,
        almacenamientoUsadoEnGigabytes: List<String>,
        listAlmacenamientoLibreGytes: List<String>
    ): List<AlmacenamientoModel> {
        val listaAlmacenamientoModel = mutableListOf<AlmacenamientoModel>()
        for (i in listaNombres.indices) {
            listaAlmacenamientoModel.add(
                AlmacenamientoModel(
                    listaNombres[i],
                    tamaniosEnGigabytes[i],
                    almacenamientoUsadoEnGigabytes[i],
                    listAlmacenamientoLibreGytes[i],
                    "",
                    "",
                    ""
                )
            )
        }
        return listaAlmacenamientoModel
    }

    private fun getAlmacenaminentoLibre(
        listaTamanios: List<Double>,
        listaAlmacenamientoUsado: List<Double>
    ): List<Double> {

        val list = mutableListOf<Double>()
        listaTamanios.zip(listaAlmacenamientoUsado).map { (tamanios, almacenamientoUsado) ->
            val libre = tamanios - almacenamientoUsado
            list.add(libre)
        }
        return list
    }

    private fun convertToGigabytes(
        listaUnidadesDeAsignacion: List<String>,
        listaTamaios: List<String>
    ): List<Double> {

        val list = mutableListOf<Double>()
        listaUnidadesDeAsignacion.zip(listaTamaios).map { (unidadesAsignacion, tamanios) ->
            list.add(Convertidor.convertToGigabytes(unidadesAsignacion, tamanios))
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

class AlmacenamientoViewModelFactory(
    private val repository: HostRepository,
    private val context: android.content.Context
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlmacenamientoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AlmacenamientoViewModel(repository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
