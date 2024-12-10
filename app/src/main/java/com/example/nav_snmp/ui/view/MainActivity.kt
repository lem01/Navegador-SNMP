package com.example.nav_snmp.ui.view

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.icu.text.DateFormat.getDateInstance
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nav_snmp.R
import com.example.nav_snmp.ui.adapters.HostAdapter
import com.example.nav_snmp.databinding.ActivityMainBinding
import com.example.nav_snmp.data.model.HostModel
import com.example.nav_snmp.data.repository.HostRepository
import com.example.nav_snmp.ui.adapters.HostAdapter.ViewHolder
import com.example.nav_snmp.ui.viewmodel.HostViewModel
import com.example.nav_snmp.ui.viewmodel.HostViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: HostViewModel

    private lateinit var binding: ActivityMainBinding
    lateinit var adapter: HostAdapter
    lateinit var hostList: ArrayList<HostModel>

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

        initAdapter()
        initObservers()
    }


    private fun initObservers() {

        viewModel.allHosts.observe(this) { hosts ->
            hosts?.let {
                hostList.clear()
                hostList.addAll(it)
                this.adapter.notifyDataSetChanged() // Notificar al adaptador para que actualice toda la lista
            }
        }
    }

    private fun initFactory() {
        // Create the repository instance
        val repository = HostRepository(applicationContext)

        // Create the ViewModel using the ViewModelProvider with the custom Factory
        val viewModelFactory = HostViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[HostViewModel::class.java]
    }


    private fun initAdapter() {
        hostList = ArrayList()
        adapter = HostAdapter(hostList, viewModel, this)
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
//                showPoppedMenuProtocoloDeBusqueda()

                Intent(this, DescubrirHostsActivity::class.java).also {
                    startActivity(it)
                }
                true
            }

            R.id.btn_tres -> {
                dialogoConfirmacionEliminar(this)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    }

    private fun dialogoConfirmacionEliminar(context: Context) {
        val builder = android.app.AlertDialog.Builder(context)
        builder.setTitle("Eliminar")
        builder.setMessage("¿Está seguro de eliminar todos los hosts?")
        builder.setPositiveButton("Si") { dialog, which ->
            viewModel.deleteAllHosts()
        }
        builder.setNegativeButton("No") { dialog, which -> }
        builder.show()
    }

    private fun showPoppedMenuProtocoloDeBusqueda() {
        val popupMenu = androidx.appcompat.widget.PopupMenu(this, binding.toolbar)

        popupMenu.menu.add(Menu.NONE, 1, Menu.NONE, "SNMP")
        popupMenu.menu.add(Menu.NONE, 2, Menu.NONE, "ICMP")

        popupMenu.show()

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                1 -> {
                    //todo
                }

                2 -> {
                    //todo

                }
            }
            true
        }


    }

    private fun hostActivity() {
        val intent = Intent(this, IpManualActivity::class.java)
        startActivity(intent)
    }


    override fun onResume() {
        super.onResume()
        viewModel.loadAllHosts()
    }

}
