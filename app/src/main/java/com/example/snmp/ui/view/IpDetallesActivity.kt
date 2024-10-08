package com.example.snmp.ui.view

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.snmp.R
import com.example.snmp.data.model.HostModel
import com.example.snmp.data.repository.HostRepository
import com.example.snmp.databinding.ActivityIpDetallesBinding
import com.example.snmp.ui.viewmodel.HostViewModel
import com.example.snmp.ui.viewmodel.HostViewModelFactory
import com.example.snmp.utils.TipoDispositivo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class IpDetallesActivity : AppCompatActivity() {
    lateinit var hostViewModel: HostViewModel

    private lateinit var binding: ActivityIpDetallesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIpDetallesBinding.inflate(layoutInflater)
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
            if (valitateFields())
                saveHost()
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
        binding.spTipo.adapter = adapter
    }

    private fun valitateFields(): Boolean {
        if (binding.etHostIp.text.isEmpty()) {
            binding.etHostIp.error = "Campo requerido"
            return false
        }

        val puerto: Int =
            if (binding.etPuerto.text.isEmpty()) 161 else binding.etPuerto.text.toString().toInt()

        if ((puerto != 161) and (puerto != 162)) {
            binding.etPuerto.error = "El puerto debe ser 161 o 162"
            return false
        }

        return true

    }

    private fun saveHost() {
        val nombreHost = binding.etHostIp.text
        val direccionIp = binding.etHostIp.text
        val versionSnmp = "v1"
        val tipoDeDispositivo = TipoDispositivo.HOST.name
        var puerto =
            if (binding.etPuerto.text.isEmpty()) 161 else binding.etPuerto.text.toString().toInt()

        if ((puerto != 161) or (puerto != 162)) {
            puerto = 161
        }
        val comunidad =
            if (binding.etComunidad.text.isEmpty()) "public" else binding.etComunidad.text.toString()


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

        hostViewModel.addHost(host)

        Toast.makeText(this, "Host guardado con éxito", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun initFactory() {
        val repository = HostRepository(applicationContext)

        val viewModelFactory = HostViewModelFactory(repository)
        hostViewModel = ViewModelProvider(this, viewModelFactory)[HostViewModel::class.java]
    }
}