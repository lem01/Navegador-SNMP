package com.example.nav_snmp.ui.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.nav_snmp.R
import com.example.nav_snmp.data.model.HostModel
import com.example.nav_snmp.data.repository.HostRepository
import com.example.nav_snmp.databinding.ActivityIpManualBinding
import com.example.nav_snmp.ui.viewmodel.HostViewModel
import com.example.nav_snmp.ui.viewmodel.HostViewModelFactory
import com.example.nav_snmp.utils.TipoDispositivo
import com.example.nav_snmp.utils.VersionSnmp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects
import java.util.TimeZone


class IpManualActivity : AppCompatActivity() {
    private lateinit var hostViewModel: HostViewModel

    private lateinit var binding: ActivityIpManualBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIpManualBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setSupportActionBar(findViewById(R.id.toolbar))
        initFactory()
        initSpinner()

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnAceptar.setOnClickListener {
            lifecycleScope.launch {
                manejadorBtnAceptar()
            }
        }

        binding.btnCancelar.setOnClickListener {
            finish()
        }

        binding.include.btnFormulario.setOnClickListener {
            if (!valitateFields())
                return@setOnClickListener

            println("Prueba de conexión")
            snmpPuebaConexion()

        }

        lifecycleScope.launch {
            initEditarHost()
            initVerHost()
        }

        binding.include.lyDescripcion.visibility = android.view.View.GONE
        ocultarToggleButton()

    }

    private fun ocultarToggleButton() {
        binding.include.toggleButtonGroup.visibility = android.view.View.GONE
    }

    private suspend fun manejadorBtnAceptar() {
        when {
            intent.getBooleanExtra("editarHost", false) -> updateHost()
            intent.getBooleanExtra("verHost", false) -> viewHost()
            else -> saveHost()
        }
    }

    private fun viewHost() {

    }

    private suspend fun updateHost() {
        var nombreHost = binding.include.etNombreHost.text.toString()
        if (nombreHost.isEmpty()) {
            nombreHost = "Genérico"
        }

        val direccionIp = binding.include.etHostIp.text
        val versionSnmp = binding.include.spVersionSnmp.selectedItem.toString()
        val tipoDeDispositivo = binding.include.spTipo.selectedItem.toString()
        var puerto =
            if (binding.include.etPuerto.text.isEmpty()) 161 else binding.include.etPuerto.text.toString()
                .toInt()

        if ((puerto != 161) or (puerto != 162)) {
            puerto = 161
        }
        val comunidad =
            if (binding.include.etComunidad.text.isEmpty()) "public" else binding.include.etComunidad.text.toString()

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale("es", "NI"))
        sdf.timeZone = TimeZone.getTimeZone("America/Managua")
        var currentDate = sdf.format(Date())

        val id = intent.getIntExtra("idHost", 0)

        val hostActual: HostModel = hostViewModel.getHostById(id)
        if (Objects.isNull(hostActual)) {
            Toast.makeText(this, "No se encontró el host", Toast.LENGTH_SHORT).show()
            return
        }

        val fechaCreacion = hostActual.fecha.toString()

        val host = HostModel(
            id,
            nombreHost,
            direccionIp.toString(),
            tipoDeDispositivo,
            versionSnmp,
            puerto,
            comunidad,
            true,
            fechaCreacion
        )

        hostViewModel.updateHost(host)

        Toast.makeText(this, "Actualizado con éxito", Toast.LENGTH_SHORT).show()

        val resultIntent = Intent()
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private suspend fun initVerHost() {
        if (intent.getBooleanExtra("verHost", false)) {
            val idHost = intent.getIntExtra("idHost", 0)
            val host = hostViewModel.getHostById(idHost)
            llenarCampos(host)
            bloquearCampos()
        }
    }

    private fun bloquearCampos() {
        binding.include.etHostIp.isEnabled = false
        binding.include.etPuerto.isEnabled = false
        binding.include.etComunidad.isEnabled = false
        binding.include.spTipo.isEnabled = false
        binding.include.spVersionSnmp.isEnabled = false
        binding.btnAceptar.isEnabled = false
        binding.include.btnFormulario.isEnabled = false
    }

    private suspend fun initEditarHost() {
        val intent = intent
        if (intent.getBooleanExtra("editarHost", false)) {
            val idHost = intent.getIntExtra("idHost", 0)
            val host = hostViewModel.getHostById(idHost)
            llenarCampos(host)
        }
    }

    private fun llenarCampos(host: HostModel) {
        binding.include.etNombreHost.setText(host.nombreHost)
        binding.include.etHostIp.setText(host.direccionIP)
        binding.include.etPuerto.setText(host.puertoSNMP.toString())
        binding.include.etComunidad.setText(host.comunidadSNMP)
        binding.include.spTipo.setSelection(TipoDispositivo.valueOf(host.tipoDeDispositivo).ordinal)
        binding.include.spVersionSnmp.setSelection(VersionSnmp.valueOf(host.versionSNMP).ordinal)
    }

    private fun snmpPuebaConexion() {
        val puerto: Int =
            if (binding.include.etPuerto.text.isEmpty()) 161 else binding.include.etPuerto.text.toString()
                .toInt()

        val comunidad = binding.include.etComunidad.text.isEmpty().let {
            if (it) "public" else binding.include.etComunidad.text.toString()
        }
        val version = binding.include.spVersionSnmp.selectedItem.toString()
        val ipHost = binding.include.etHostIp.text.toString()
        val tipoDispositivo = binding.include.spTipo.selectedItem.toString()

        val hostModel =
            HostModel(0, ipHost, ipHost, tipoDispositivo, version, puerto, comunidad, true, "")

        when (version) {
            VersionSnmp.V1.name -> {
                hostViewModel.snmpV1Test(hostModel, this)
            }

            VersionSnmp.V2c.name -> {
                hostViewModel.snmpV2cTest(hostModel, this)
            }
        }

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
    }

    private fun valitateFields(): Boolean {
        if (binding.include.etHostIp.text.isEmpty()) {
            binding.include.etHostIp.error = "Campo requerido"
            return false
        }

        val puerto: Int =
            if (binding.include.etPuerto.text.isEmpty()) 161 else binding.include.etPuerto.text.toString()
                .toInt()

        if ((puerto != 161) and (puerto != 162)) {
            binding.include.etPuerto.error = "El puerto debe ser 161 o 162"
            return false
        }

        return true

    }

    private fun saveHost() {
        var nombreHost = binding.include.etNombreHost.text.toString()
        if (nombreHost.isEmpty()) {
            nombreHost = "Genérico"
        }

        val direccionIp = binding.include.etHostIp.text
        val versionSnmp = binding.include.spVersionSnmp.selectedItem.toString()
        val tipoDeDispositivo = binding.include.spTipo.selectedItem.toString()
        var puerto =
            if (binding.include.etPuerto.text.isEmpty()) 161 else binding.include.etPuerto.text.toString()
                .toInt()

        if ((puerto != 161) or (puerto != 162)) {
            puerto = 161
        }
        val comunidad =
            if (binding.include.etComunidad.text.isEmpty()) "public" else binding.include.etComunidad.text.toString()


        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale("es", "NI"))
        sdf.timeZone = TimeZone.getTimeZone("America/Managua")
        val currentDate = sdf.format(Date())

        val host = HostModel(
            0,
            nombreHost.toString(),
            direccionIp.toString(),
            tipoDeDispositivo,
            versionSnmp,
            puerto,
            comunidad,
            true,
            currentDate
        )

        hostViewModel.saveHost(host)

        Toast.makeText(this, "Host guardado con éxito", Toast.LENGTH_SHORT).show()

        val resultIntent = Intent()
        setResult(Activity.RESULT_OK, resultIntent)
        finish()

    }

    private fun initFactory() {
        val repository = HostRepository(applicationContext)
        val viewModelFactory = HostViewModelFactory(repository)
        hostViewModel = ViewModelProvider(this, viewModelFactory)[HostViewModel::class.java]
    }
}