package com.example.snmp.ui.view

import android.content.Intent
import android.graphics.PorterDuff
import android.icu.text.DateFormat.getDateInstance
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.snmp.R
import com.example.snmp.ui.adapters.HostAdapter
import com.example.snmp.databinding.ActivityMainBinding
import com.example.snmp.data.model.HostModel
import com.example.snmp.data.repository.HostRepository
import com.example.snmp.ui.viewmodel.HostViewModel
import com.example.snmp.ui.viewmodel.HostViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var adapter: HostAdapter
    lateinit var hostList: ArrayList<HostModel>
    lateinit var hostViewModel: HostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        setSupportActionBar(findViewById(R.id.toolbar))

        initFactory()

        hostList = ArrayList()
        initAdapter()
        initComponents()

        binding.fab.setOnClickListener {
//todo:

//            hostList.add(
//                HostModel(
//                    0,
//                    "nombreHost",
//                    "direccionIP",
//                    "tipoDeDispositivo",
//                    "versionSNMP",
//                    161,
//                    "comunidadSNMP",
//                    true,
//                    "fecha"
//                )
//            )

            //datetime dia/mes/año sin hora
            val formatter = getDateInstance()
            val dateString = formatter.format(java.util.Date())

            hostViewModel.addHost(
                HostModel(
                    0,
                    "Lennox",
                    "192.168.1.1",
                    "tipoDeDispositivo",
                    "V1",
                    161,
                    "public",
                    true,
                    dateString
                )
            )



            adapter.notifyItemChanged(hostList.size - 1)
        }
    }

    private fun initFactory() {
        // Create the repository instance
        val repository = HostRepository(applicationContext)

        // Create the ViewModel using the ViewModelProvider with the custom Factory
        val viewModelFactory = HostViewModelFactory(repository)
        hostViewModel = ViewModelProvider(this, viewModelFactory)[HostViewModel::class.java]
    }

    private fun initComponents() {
    }

    private fun initAdapter() {
        adapter = HostAdapter(hostList, hostViewModel)
        binding.rcHost.layoutManager = LinearLayoutManager(this)
        binding.rcHost.adapter = adapter

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            val drawable = item.icon
            if (drawable != null) {
                drawable.mutate()
                drawable.setColorFilter(
                    ContextCompat.getColor(this, R.color.white),
                    PorterDuff.Mode.SRC_ATOP
                )
            }
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.btn_uno -> {
                hostActivity()

                true
            }

            R.id.btn_dos -> {

                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun hostActivity() {
        val intent = Intent(this, IpDetallesActivity::class.java)
        startActivity(intent)
    }


    override fun onResume() {
        super.onResume()
        hostViewModel.allHosts.observe(this) { hosts ->
            hosts?.let {
                hostList.clear()
                hostList.addAll(it)
                adapter.notifyItemChanged(hostList.size - 1)
            }
        }
    }


}