package com.example.nav_snmp.ui.view

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nav_snmp.R
import com.example.nav_snmp.databinding.ActivityDescubrirHostsBinding
import com.example.nav_snmp.ui.view.descubrir_host_detalles.DescubrirHostDetalles
import com.example.nav_snmp.utils.Constantes
import com.example.nav_snmp.utils.TipoDispositivo
import com.example.nav_snmp.utils.VersionSnmp

class DescubrirHostsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDescubrirHostsBinding
    lateinit var preferences: SharedPreferences;
    lateinit var editor: SharedPreferences.Editor;

    enum class tipoDeBusqueda {
        SNMP, ICMP
    }

    var tipoBusqueda = tipoDeBusqueda.SNMP

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDescubrirHostsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setSupportActionBar(findViewById(R.id.toolbar))
        initPrefences()
        initSpinner()
        initToogleButton()
        initLabelsCoponents()

        binding.include.lyTipoDispositivo.visibility = android.view.View.GONE

        binding.include.btnFormulario.text = "Descubrir Hosts"
        binding.include.btnFormulario.setOnClickListener() {

            if (!valitateFields())
                return@setOnClickListener

            val puerto = if (binding.include.etPuerto.text.toString()
                    .isEmpty()
            ) "161" else binding.include.etPuerto.text.toString()
            val comunidad = if (binding.include.etComunidad.text.toString()
                    .isEmpty()
            ) "public" else binding.include.etComunidad.text.toString()

            editor.putString(Constantes.SNMP_RANGO_IP, binding.include.etHostIp.text.toString())
            editor.putString(Constantes.SNMP_COMUNIDAD, comunidad)
            editor.putString(Constantes.SNMP_PUERTO, puerto)
            editor.putString(
                Constantes.SNMP_VERSION,
                binding.include.spVersionSnmp.selectedItem.toString()
            )
            editor.apply()

            val intent = android.content.Intent(this, DescubrirHostDetalles::class.java)
            startActivity(intent)
        }

        binding.include.lyTipoDispositivo.visibility = android.view.View.GONE


    }

    private fun initPrefences() {
        preferences = getSharedPreferences("configuracion", android.content.Context.MODE_PRIVATE)
        editor = preferences.edit()

    }

    private fun initLabelsCoponents() {
        binding.include.tvHostIp.text = "Rango IP "
    }

    private fun initToogleButton() {
        binding.include.toggleButtonGroup.check(R.id.button1)

        binding.include.toggleButtonGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->

            if (isChecked) {
                when (checkedId) {
                    R.id.button1 -> {
                        cambiarVisivilidadComponentes(true)
                        tipoBusqueda = tipoDeBusqueda.SNMP
                    }

                    R.id.button2 -> {
                        cambiarVisivilidadComponentes(false)
                        tipoBusqueda = tipoDeBusqueda.ICMP
                    }
                }
            }
        }
    }

    private fun cambiarVisivilidadComponentes(b: Boolean) {
        if (b) {
            binding.include.lyComunidad.visibility = android.view.View.VISIBLE
            binding.include.lyPuerto.visibility = android.view.View.VISIBLE
            binding.include.lyTipoDispositivo.visibility = android.view.View.VISIBLE
            binding.include.lyVersionSnmp.visibility = android.view.View.VISIBLE

        } else {
            binding.include.lyComunidad.visibility = android.view.View.GONE
            binding.include.lyPuerto.visibility = android.view.View.GONE
            binding.include.lyTipoDispositivo.visibility = android.view.View.GONE
            binding.include.lyVersionSnmp.visibility = android.view.View.GONE
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
}