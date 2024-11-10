package com.example.nav_snmp.ui.view.graficos

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nav_snmp.R
import com.example.nav_snmp.databinding.ActivityHerramientasBinding
import com.example.nav_snmp.ui.view.icmp.IcmpActivity
import com.example.nav_snmp.ui.view.interfaces_de_red.InterfacesDeRedActivity
import com.example.nav_snmp.ui.view.sistema.SistemaActivity
import com.example.nav_snmp.ui.view.tcp.TcpActivity
import com.example.nav_snmp.ui.view.udp.UdpActivity

class HerramientasActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHerramientasBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityHerramientasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.cardInformacionDeSitema.setOnClickListener {
            val intent = Intent(this, SistemaActivity::class.java)
            startActivity(intent)
        }

        binding.cardViewIcmp.setOnClickListener {
            val intent = Intent(this, IcmpActivity::class.java)
            startActivity(intent)
        }

        binding.cardViewTcp.setOnClickListener {
            val intent = Intent(this, TcpActivity::class.java)
            startActivity(intent)
        }

        binding.cardViewUdp.setOnClickListener {
            val intent = Intent(this, UdpActivity::class.java)
            startActivity(intent)
        }

        binding.cardViewInterfacesDeRed.setOnClickListener {
            val intent = Intent(this, InterfacesDeRedActivity::class.java)
            startActivity(intent)
        }
    }

}
