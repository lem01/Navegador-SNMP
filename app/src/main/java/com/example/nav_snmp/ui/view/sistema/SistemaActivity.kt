package com.example.nav_snmp.ui.view.sistema

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.nav_snmp.R
import com.example.nav_snmp.databinding.ActivitySistemaBinding
import com.google.android.material.tabs.TabLayout

class SistemaActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySistemaBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySistemaBinding.inflate(layoutInflater)
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
                    0 -> navController.navigate(R.id.informacion_sitema_fragment)
                    1 -> navController.navigate(R.id.almacenamiento_fragment)
                    2 -> navController.navigate(R.id.procesos_fragment)
                }
            }


            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // no-op
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // no-op
            }
        })


    }

    private fun initNavController() {
        navController = findNavController(R.id.nav_host_fragment)
        binding.tabLayout.getTabAt(navController.graph.startDestinationId)

    }
}