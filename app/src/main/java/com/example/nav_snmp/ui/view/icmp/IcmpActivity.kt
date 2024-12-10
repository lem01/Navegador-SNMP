package com.example.nav_snmp.ui.view.icmp

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.nav_snmp.R
import com.example.nav_snmp.databinding.ActivityIcmpBinding
import com.example.nav_snmp.databinding.ActivitySistemaBinding
import com.google.android.material.tabs.TabLayout

class IcmpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIcmpBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityIcmpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        initNavController()
        initNav()

        // Maneja el evento de retroceso del sistema
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish() // Finaliza la actividad
            }
        })
    }

    private fun initNav() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> navController.navigate(R.id.icmpEntradaFragment)
                    1 -> navController.navigate(R.id.icmpSalidaFragment)
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
