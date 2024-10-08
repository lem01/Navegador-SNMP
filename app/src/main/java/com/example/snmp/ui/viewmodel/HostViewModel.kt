package com.example.snmp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.snmp.data.model.HostModel
import com.example.snmp.data.repository.HostRepository
import kotlinx.coroutines.launch


class HostViewModel(private val repository: HostRepository) : ViewModel() {
    
    val allHosts = repository.getAllHosts().asLiveData()

    fun addHost(host: HostModel) {
        viewModelScope.launch {
            repository.saveHost(host)
        }
    }
}
