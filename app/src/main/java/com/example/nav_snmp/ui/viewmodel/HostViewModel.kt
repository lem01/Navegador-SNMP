package com.example.nav_snmp.ui.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


import androidx.lifecycle.viewModelScope
import com.example.nav_snmp.data.model.HostModel
import com.example.nav_snmp.data.repository.HostRepository
import com.example.nav_snmp.utils.SnmpManagerV1
import com.example.nav_snmp.utils.SnmpManagerV2c
import kotlinx.coroutines.launch

import androidx.lifecycle.ViewModel
import com.example.nav_snmp.data.model.OperacionModel
import com.example.nav_snmp.utils.CommonOids
import com.example.nav_snmp.utils.Convertidor
import com.example.nav_snmp.utils.TipoOperacion


class HostViewModel(private val repository: HostRepository) : ViewModel() {

    private val _operacionModel = MutableLiveData<OperacionModel>()
    val operacionModel: LiveData<OperacionModel> get() = _operacionModel

//    private val _respuestaOperacionSnmp = MutableLiveData<String>()
//    val respuestaOperacion: LiveData<String> get() = _respuestaOperacionSnmp

    private val _allHosts = MutableLiveData<List<HostModel>>()
    val allHosts: LiveData<List<HostModel>> get() = _allHosts

    fun saveHost(host: HostModel) {
        viewModelScope.launch {
            repository.saveHost(host)
        }

    }

    fun snmpV1Test(hostModel: HostModel, context: Context) {
        if (isvalidIp(hostModel.direccionIP)) {
            val handler = android.os.Handler()
            handler.postDelayed({
                val snmpManagerV1 = SnmpManagerV1()
                snmpManagerV1.snmpTest(hostModel, context)
            }, 3000)
        }

    }

    fun snmpV2cTest(hostModel: HostModel, context: Context) {
        if (isvalidIp(hostModel.direccionIP)) {
            val snmpManagerV2c = SnmpManagerV2c()
            snmpManagerV2c.snmpTest(hostModel, context)
        }
    }

    fun detelteHost(id: Int) {
        viewModelScope.launch {
            repository.deleteHost(id)
        }
    }

    fun loadAllHosts() {
        viewModelScope.launch {
            val hosts = repository.getAllHosts()
            _allHosts.postValue(hosts)
        }
    }

    suspend fun getHostById(idHost: Int): HostModel {
        return repository.getHostById(idHost)
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

    fun updateHost(host: HostModel) {
        viewModelScope.launch {
            repository.updateHost(host)
        }
    }

    suspend fun operacionSnmp(
        hostModel: HostModel,
        oid: String,
        tipoOperacion: TipoOperacion,
        context: Context
    ) {
        if (isvalidIp(hostModel.direccionIP)) {
            val snmpManagerV1 = SnmpManagerV1()
//            snmpManagerV1.operacionSnmp(hostModel, oid, tipoOperacion, context)

            when (tipoOperacion) {
                TipoOperacion.GET -> {
                    var respuesta = snmpManagerV1.get(hostModel, oid, tipoOperacion, context)
                    respuesta = Convertidor.getFormater2(respuesta, oid)

                    _operacionModel.postValue(
                        OperacionModel(
                            respuesta,
                            oid,
                        )
                    )
                    println("Respuesta: $respuesta")
//                    _respuestaOperacionSnmp.postValue(respuesta)
                    Toast.makeText(context, respuesta, Toast.LENGTH_SHORT).show()
                }

                TipoOperacion.GET_NEXT -> {
                    val respuesta =
                        snmpManagerV1.getNext(
                            hostModel,
                            oid,
                            tipoOperacion,
                            context,
                            true
                        ) as HashMap<*, *>
//                    respuesta = Convertidor.getFormater(respuesta.toString(), oid) as HashMap<String, String>

                    (respuesta as HashMap<String, String>)["valor"] =
                        Convertidor.getFormater2(
                            respuesta["valor"] ?: "", respuesta["oid"] ?: ""
                        )
                    _operacionModel.postValue(
                        OperacionModel(
                            respuesta["valor"].toString(),
                            respuesta["oid"].toString()
                        )
                    )

                }

                TipoOperacion.SET -> TODO()
            }

        }
    }
}
