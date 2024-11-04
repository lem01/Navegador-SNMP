package com.example.nav_snmp

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.nav_snmp.data.model.HostModel
import com.example.nav_snmp.data.repository.HostRepository
import com.example.nav_snmp.databinding.ActivityIpManualBinding
import com.example.nav_snmp.databinding.ActivityOperacionSnmpBinding
import com.example.nav_snmp.ui.viewmodel.HostViewModel
import com.example.nav_snmp.ui.viewmodel.HostViewModelFactory
import com.example.nav_snmp.utils.TipoDispositivo
import com.example.nav_snmp.utils.TipoOperacion
import com.example.nav_snmp.utils.VersionSnmp
import kotlinx.coroutines.launch

class OperacionSnmpActivity : AppCompatActivity() {
    private lateinit var hostViewModel: HostViewModel
    private lateinit var binding: ActivityOperacionSnmpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOperacionSnmpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(findViewById(R.id.toolbar))
        initFactory()
        initSpinner()

        ocultarComponentes()
        mostrarComponentes()
        lifecycleScope.launch {
            initComponents()
        }

        binding.include.btnFormulario.setOnClickListener {

            if (!validarCampos()) {
                return@setOnClickListener
            }

        }
    }

    private fun validarCampos(): Boolean {
        var isValid = true

        if (binding.include.etComunidad.text.isEmpty()) {
            binding.include.etComunidad.error = "Campo requerido"
            isValid = false
        }

        if (binding.include.etHostIp.text.isEmpty()) {
            binding.include.etHostIp.error = "Campo requerido"
            isValid = false
        }

        val puerto: Int = if (binding.include.etPuerto.text.isEmpty()) 161 else binding.include.etPuerto.text.toString().toInt()
        if (puerto != 161 && puerto != 162) {
            binding.include.etPuerto.error = "El puerto debe ser 161 o 162"
            isValid = false
        }

        if (binding.include.etOid.text.isEmpty()) {
            binding.include.etOid.error = "Campo requerido"
            isValid = false
        }

        return isValid
    }

    private fun mostrarComponentes() {
        binding.include.lyTipoOperacion.visibility = android.view.View.VISIBLE
        binding.include.lyDescripcion.visibility = android.view.View.VISIBLE
        binding.include.lyVersionSnmp.visibility = android.view.View.VISIBLE
        binding.include.lyOid.visibility = android.view.View.VISIBLE
    }

    private fun ocultarComponentes() {
        binding.include.toggleButtonGroup.visibility = android.view.View.GONE
    }

    private fun initSpinner() {
        val items = ArrayList<String>()
        for (tipo in TipoDispositivo.entries) {
            items.add(tipo.name)
        }

        items.remove(TipoDispositivo.HOST.name)
        items.add(0, TipoDispositivo.HOST.name)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.include.spTipo.adapter = adapter


        val versionSnmp = ArrayList<String>()
        versionSnmp.add(VersionSnmp.V1.name)
        versionSnmp.add(VersionSnmp.V2c.name)
        val adapterVersion =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, versionSnmp)
        adapterVersion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.include.spVersionSnmp.adapter = adapterVersion


        val listaOperaciones = ArrayList<String>()

        listaOperaciones.add(TipoOperacion.GET.name)
        listaOperaciones.add(TipoOperacion.GET_NEXT.name)

        val adapterTipoOperacion =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, listaOperaciones)
        adapterTipoOperacion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.include.spTipoOperacion.adapter = adapterTipoOperacion

    }

    private suspend fun initComponents() {
        val idHost = intent.getIntExtra("id", 0)
        val host = hostViewModel.getHostById(idHost)
        llenarCampos(host)
        bloquearCampos()
        cambiarTextos()
    }

    private fun cambiarTextos() {
        binding.include.btnFormulario.setText("Hacer  operacion")
    }

    private fun llenarCampos(host: HostModel) {
        binding.include.etNombreHost.setText(host.nombreHost)
        binding.include.etHostIp.setText(host.direccionIP)
        binding.include.etPuerto.setText(host.puertoSNMP.toString())
        binding.include.etComunidad.setText(host.comunidadSNMP)
        binding.include.spTipo.setSelection(TipoDispositivo.valueOf(host.tipoDeDispositivo).ordinal)
        binding.include.spVersionSnmp.setSelection(VersionSnmp.valueOf(host.versionSNMP).ordinal)
    }

    private fun bloquearCampos() {
        binding.include.etNombreHost.isEnabled = false
        binding.include.etHostIp.isEnabled = false
        binding.include.spTipo.isEnabled = false
        binding.include.edDescripcion.isEnabled = false
        binding.include.edDescripcion.isFocusable = false

        binding.include.btnFormulario.isEnabled = true


    }

    private fun initFactory() {
        val repository = HostRepository(applicationContext)
        val viewModelFactory = HostViewModelFactory(repository)
        hostViewModel = ViewModelProvider(this, viewModelFactory)[HostViewModel::class.java]
    }
}
