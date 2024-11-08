package com.example.nav_snmp.ui.view.icmp.fragments.icmp_salida_fragment

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import com.example.nav_snmp.data.model.HostModel
import com.example.nav_snmp.data.model.IcmpEntradaModel
import com.example.nav_snmp.data.model.IcmpSalidaModel
import com.example.nav_snmp.data.model.TcpModel
import com.example.nav_snmp.data.repository.HostRepository
import com.example.nav_snmp.utils.CommonOids
import com.example.nav_snmp.utils.Convertidor
import com.example.nav_snmp.utils.Preferencias
import com.example.nav_snmp.utils.SnmpManagerV1
import com.example.nav_snmp.utils.TipoOperacion
import com.example.nav_snmp.utils.VersionSnmp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class IcmpSalidaViewModel(
    private val repository: HostRepository,
    private val context: Context
) : ViewModel() {
    private val TAG = "IcmpSalidaViewModel"

    private val _icmpModel = MutableLiveData<IcmpSalidaModel>()
    val icmpModel: MutableLiveData<IcmpSalidaModel>
        get() = _icmpModel

    private val _showDatos = MutableLiveData<Boolean>()
    val showDatos: MutableLiveData<Boolean> get() = _showDatos

    private val _barraProgreso = MutableLiveData<Boolean>()
    val barraProgreso: MutableLiveData<Boolean> get() = _barraProgreso

    suspend fun cargarDatosSistema(requireContext: Context) {

        barraProgreso.postValue(true)
        _showDatos.postValue(false)

        val preferences = requireContext.getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val id = preferences.getInt(Preferencias.ID_HOST, 0)
        val host: HostModel = getHostById(id)
//
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

                val Enviados = snmpManagerV1.get(
                    host,
                    CommonOids.ICMP.ICMP_OUT_MSGS,
                    TipoOperacion.GET,
                    context,
                    false
                )

                var smpConError = snmpManagerV1.get(
                    host,
                    CommonOids.ICMP.ICMP_OUT_ERRORS,
                    TipoOperacion.GET,
                    context,
                    false
                )

                val smpDestinoNoAlcanzable = snmpManagerV1.get(
                    host,
                    CommonOids.ICMP.ICMP_OUT_DEST_UNREACH,
                    TipoOperacion.GET,
                    context,
                    false
                )

                val smpTiempoExcedente = snmpManagerV1.get(
                    host,
                    CommonOids.ICMP.ICMP_OUT_TIME_EXCDS,
                    TipoOperacion.GET,
                    context,
                    false
                )

                val smpProblemasDeParametros = snmpManagerV1.get(
                    host,
                    CommonOids.ICMP.ICMP_OUT_PROBS,
                    TipoOperacion.GET,
                    context,
                    false
                )

                var controlDeFlujo = snmpManagerV1.get(
                    host,
                    CommonOids.ICMP.ICMP_OUT_SRC_QENCHS,
                    TipoOperacion.GET,
                    context,
                    false
                )

                var smsDeRedireccion = snmpManagerV1.get(
                    host,
                    CommonOids.ICMP.ICMP_OUT_REDIRECTS,
                    TipoOperacion.GET,
                    context,
                    false
                )

                var smpEco = snmpManagerV1.get(
                    host,
                    CommonOids.ICMP.ICMP_OUT_ECHOS,
                    TipoOperacion.GET,
                    context,
                    false
                )

                var smsMarcaDeTiempo = snmpManagerV1.get(
                    host,
                    CommonOids.ICMP.ICMP_OUT_TIMESTAMPS,
                    TipoOperacion.GET,
                    context,
                    false
                )

                _icmpModel.value = IcmpSalidaModel(
                    Enviados,
                    smpConError,
                    smpDestinoNoAlcanzable,
                    smpTiempoExcedente,
                    smpProblemasDeParametros,
                    controlDeFlujo,
                    smsDeRedireccion,
                    smpEco,
                    smsMarcaDeTiempo
                )

                withContext(Dispatchers.Main) {
//                    Toast.makeText(context, "Datos cargados ${_icmpModel.map {  " ${it.}" }}", Toast.LENGTH_SHORT).show()
                }
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

class IcmpSalidaViewModelFactory(
    private val repository: HostRepository,
    private val context: Context
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(IcmpSalidaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return IcmpSalidaViewModel(repository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
