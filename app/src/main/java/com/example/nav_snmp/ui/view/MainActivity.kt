package com.example.nav_snmp.ui.view

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
import com.example.nav_snmp.ui.viewmodel.HostViewModel
import com.example.nav_snmp.ui.viewmodel.HostViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetAddress

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

//        lifecycleScope.launch {
//        }

        initAdapter()
        initObservers()

        binding.fab.setOnClickListener {
            lifecycleScope.launch {
                val isReachable = withContext(Dispatchers.IO) { ping("192.168.1.101") }

                withContext(Dispatchers.Main) {
                    // Show the result in a Toast on the main thread
                    if (isReachable) {
                        Log.d("MainActivity", "The host is reachable.")
                        Toast.makeText(
                            applicationContext,
                            "The host is reachable.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Log.d("MainActivity", "The host is not responding.")
                        Toast.makeText(
                            applicationContext,
                            "The host is not responding.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun ping(ipAddress: String): Boolean {
        return try {
            val inetAddress = InetAddress.getByName(ipAddress) // Usar la IP pasada como argumento
            inetAddress.isReachable(3000) // Establecer el timeout de 5 segundos

        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    private fun initObservers() {

        viewModel.allHosts.observe(this) { hosts ->
            hosts?.let {
                hostList.clear()
                hostList.addAll(it)
                adapter.notifyItemChanged(hostList.size - 1) // Notificar al adaptador para que actualice toda la lista
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

            else -> super.onOptionsItemSelected(item)
        }

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
//        viewModel.loadAllHosts()
        viewModel.loadAllHosts()

    }

}
