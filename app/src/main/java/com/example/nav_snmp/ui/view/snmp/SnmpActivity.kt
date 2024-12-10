package com.example.nav_snmp.ui.view.snmp

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.nav_snmp.R
import com.example.nav_snmp.data.repository.HostRepository
import com.example.nav_snmp.databinding.ActivityMainBinding
import com.example.nav_snmp.databinding.ActivitySnmpBinding
import com.example.nav_snmp.ui.viewmodel.HostViewModel
import com.example.nav_snmp.ui.viewmodel.HostViewModelFactory
import com.google.android.material.tabs.TabLayout

class SnmpActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivitySnmpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySnmpBinding.inflate(layoutInflater)
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

        initNavController()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> navController.navigate(R.id.snmpRecibidosFragment)
                    1 -> navController.navigate(R.id.snmpEnviadosFragment)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // no-op
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // no-op
            }
        })

        // Maneja el evento de retroceso del sistema
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish() // Finaliza la actividad
            }
        })
    }

    private fun initNavController() {
        navController = findNavController(R.id.nav_host_fragment)
        binding.tabLayout.getTabAt(navController.graph.startDestinationId)
    }
}
