package com.example.snmp.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData


import androidx.lifecycle.viewModelScope
import com.example.snmp.data.model.HostModel
import com.example.snmp.data.model.HostModelClass
import com.example.snmp.data.repository.HostRepository
import com.example.snmp.utils.SnmpManagerV1
import com.example.snmp.utils.SnmpManagerV2c
import kotlinx.coroutines.launch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope


class HostViewModel(private val repository: HostRepository) : ViewModel() {


    private val _allHosts = MutableLiveData<List<HostModel>>()
    val allHosts: LiveData<List<HostModel>> get() = _allHosts

    fun addHost(host: HostModel) {
        viewModelScope.launch {
            repository.saveHost(host)
        }
    }

    fun snmpV1Test(hostModel: HostModel, context: Context) {
        if (isvalidIp(hostModel.direccionIP)) {
            val snmpManagerV1 = SnmpManagerV1()
            snmpManagerV1.snmpTest(hostModel, context)
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
}

