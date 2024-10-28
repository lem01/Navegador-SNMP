package com.example.nav_snmp.ui.view.descubrir_host_detalles

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.nav_snmp.data.repository.HostRepository
import com.example.nav_snmp.ui.theme.SnmpTheme
import com.example.nav_snmp.ui.viewmodel.DescubrirHostViewModel
import com.example.nav_snmp.ui.viewmodel.DescubrirHostViewModelFactory
import com.example.nav_snmp.utils.Constantes
import kotlin.math.log


class DescubrirHostDetalles : ComponentActivity() {
    lateinit var preferences: SharedPreferences;
    lateinit var editor: SharedPreferences.Editor;
    var context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPrefences()
        initFactory()

        enableEdgeToEdge()
        setContent {

            SnmpTheme {
                HostDetallesScreen(Modifier.fillMaxSize(), context)
            }
        }
    }

    private fun initPrefences() {
        preferences = getSharedPreferences("configuracion", android.content.Context.MODE_PRIVATE)
        editor = preferences.edit()

    }

    private fun initFactory() {
        val repository = HostRepository(applicationContext)

        val viewModelFactory = DescubrirHostViewModelFactory(repository, applicationContext)
        DescubrirHostViewModel.descubrirHost =
            ViewModelProvider(this, viewModelFactory)[DescubrirHostViewModel::class.java]
    }
}
