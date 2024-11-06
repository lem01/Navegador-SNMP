package com.example.nav_snmp.ui.view.graficos

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.nav_snmp.R
import com.example.nav_snmp.databinding.ActivityGraficosAtivityBinding
import com.example.nav_snmp.ui.view.sistema.SistemaActivity

class GraficosAtivity : AppCompatActivity() {
    private lateinit var binding: ActivityGraficosAtivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityGraficosAtivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.toolbar.setNavigationOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }

        binding.cardInformacionDeSitema.setOnClickListener {
            val intent = Intent(this, SistemaActivity::class.java)
            startActivity(intent)
        }
    }


}