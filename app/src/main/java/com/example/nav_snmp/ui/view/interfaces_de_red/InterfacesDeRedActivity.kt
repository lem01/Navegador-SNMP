package com.example.nav_snmp.ui.view.interfaces_de_red

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nav_snmp.R
import com.example.nav_snmp.data.model.InterfacesDeRedModel
import com.example.nav_snmp.data.repository.HostRepository
import com.example.nav_snmp.databinding.ActivityInterfacesDeRedBinding
import com.example.nav_snmp.databinding.ActivityMainBinding
import com.example.nav_snmp.ui.adapters.InterfacesDeRedAdapter
import com.example.nav_snmp.ui.adapters.TablaDeConexionesUDPAdapter
import com.example.nav_snmp.ui.view.tcp.fragments.tabla_de_conexiones_fragment.TablaDeConexionesTCPViewModel
import com.example.nav_snmp.ui.viewmodel.HostViewModel
import com.example.nav_snmp.ui.viewmodel.HostViewModelFactory
import kotlinx.coroutines.launch

class InterfacesDeRedActivity : AppCompatActivity() {
    val TAG = "InterfacesDeRedActivity"
    private lateinit var binding: ActivityInterfacesDeRedBinding
    private lateinit var viewModel: InterfacesDeRedViewModel
    private lateinit var adapter: InterfacesDeRedAdapter
    private lateinit var list: ArrayList<InterfacesDeRedModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityInterfacesDeRedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.toolbar.setNavigationOnClickListener {
//            onBackPressedDispatcher.onBackPressed()
            //salir de esta actividad
            finish()
        }
        // Maneja el evento de retroceso del sistema
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish() // Finaliza la actividad
            }
        })

        initFactory()

        lifecycleScope.launch {
            viewModel.cargarDatos(applicationContext)
        }

        initBarraProgreso(viewModel)
        initShowDatos(viewModel)
        initAdapter()
        initObservers(viewModel)
    }

    private fun initObservers(viewModel: InterfacesDeRedViewModel) {
        viewModel.interfacesDeRedModel.observe(this) {
            list.clear()
            list.addAll(it)
            this.adapter.notifyDataSetChanged()
        }
    }

    private fun initAdapter() {
        list = ArrayList()
        adapter = InterfacesDeRedAdapter(list, viewModel, applicationContext)
        binding.recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        binding.recyclerView.adapter = adapter
    }

    private fun initShowDatos(viewModel: InterfacesDeRedViewModel) {
        viewModel.showDatos.observe(this) {
            if (it) {
                binding.tableLayout.visibility = View.VISIBLE

            } else {
                binding.tableLayout.visibility = View.GONE

            }
        }
    }


    private fun initBarraProgreso(viewModel: InterfacesDeRedViewModel) {
        viewModel.barraProgreso.observe(this) {
            if (it) {
                binding.linearProgressIndicator.visibility = View.VISIBLE
            } else {
                binding.linearProgressIndicator.visibility = View.GONE
            }
        }
    }

    private fun initFactory() {
        val repository = HostRepository(applicationContext)

        val viewModelFactory =
            InterfacesDeRedViewModelFactory(repository, applicationContext)
        viewModel =
            ViewModelProvider(
                this,
                viewModelFactory
            )[InterfacesDeRedViewModel::class.java]
    }
}
