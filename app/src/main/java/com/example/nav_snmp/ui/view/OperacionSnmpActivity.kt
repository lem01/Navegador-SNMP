package com.example.nav_snmp.ui.view

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.nav_snmp.R
import com.example.nav_snmp.data.model.HostModel
import com.example.nav_snmp.data.repository.HostRepository
import com.example.nav_snmp.databinding.ActivityOperacionSnmpBinding
import com.example.nav_snmp.ui.viewmodel.HostViewModel
import com.example.nav_snmp.ui.viewmodel.HostViewModelFactory
import com.example.nav_snmp.utils.CommonOids
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

        initObservers()

        ocultarComponentes()
        mostrarComponentes()
        initComponents()

        binding.include.btnFormulario.setOnClickListener {
            if (!validarCampos()) return@setOnClickListener
            lifecycleScope.launch {
                operacionSnmp()
            }
        }
    }

    private fun initObservers() {
        hostViewModel.operacionModel.observe(this, Observer {
            binding.include.edDescripcion.setText(it.valor)
            binding.include.etOid.setText(it.oid)
        })
    }

    private suspend fun operacionSnmp() {
        val puerto: Int =
            if (binding.include.etPuerto.text.isEmpty()) 161 else binding.include.etPuerto.text.toString()
                .toInt()

        val comunidad = binding.include.etComunidad.text.isEmpty().let {
            if (it) "public" else binding.include.etComunidad.text.toString()
        }
        val version = binding.include.spVersionSnmp.selectedItem.toString()
        val ipHost = binding.include.etHostIp.text.toString()
        val tipoDispositivo = binding.include.spTipo.selectedItem.toString()
        val oid = binding.include.etOid.text.toString()
        val hostModel =
            HostModel(0, ipHost, ipHost, tipoDispositivo, version, puerto, comunidad, true, "")

        val tipoOperacion: TipoOperacion =
            TipoOperacion.valueOf(binding.include.spTipoOperacion.selectedItem.toString())
        when (version) {
            VersionSnmp.V1.name -> {
                hostViewModel.operacionSnmp(
                    hostModel, oid,
                    tipoOperacion,
                    this
                )
            }

            VersionSnmp.V2c.name -> {
                hostViewModel.snmpV2cTest(hostModel, this)
            }
        }

    }

    private fun validarCampos(): Boolean {
        var isValid = true
        if (binding.include.etHostIp.text.isEmpty()) {
            binding.include.etHostIp.error = "Campo requerido"
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
        //todo: quitar cuando este listo snmp v2c
//        versionSnmp.add(VersionSnmp.V2c.name)
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

    private fun initComponents() {
        lifecycleScope.launch {
            val idHost = intent.getIntExtra("id", 0)
            val host = hostViewModel.getHostById(idHost)
            llenarCampos(host)
            bloquearCampos()
            cambiarTextos()
        }
    }

    private fun cambiarTextos() {
        binding.include.btnFormulario.setText("Hacer  operacion")
    }

    private fun llenarCampos(host: HostModel) {
        binding.include.etOid.setText(CommonOids.SYSTEM.SYS_DESCR)
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
//        binding.include.edDescripcion.isEnabled = false
//        binding.include.edDescripcion.isFocusable = false

        binding.include.btnFormulario.isEnabled = true


    }

    private fun initFactory() {
        val repository = HostRepository(applicationContext)
        val viewModelFactory = HostViewModelFactory(repository)
        hostViewModel = ViewModelProvider(this, viewModelFactory)[HostViewModel::class.java]
    }
}
