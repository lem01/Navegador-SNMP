package com.example.snmp.ui.view

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.snmp.R
import com.example.snmp.databinding.ActivityDescubrirHostsBinding
import com.example.snmp.utils.TipoDispositivo

class DescubrirHostsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDescubrirHostsBinding

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
        initSpinner()
        initToogleButton()
        initLabelsCoponents()

        binding.include.lyTipoDispositivo.visibility = android.view.View.GONE

        binding.include.btnConexionPrueba.setOnClickListener(){
            val intent = android.content.Intent(this, DescubrirHostDetalles::class.java)
            startActivity(intent)
        }


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

}