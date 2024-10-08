package com.example.snmp.ui.view

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.snmp.R
import com.example.snmp.ui.adapters.HostAdapter
import com.example.snmp.databinding.ActivityMainBinding
import com.example.snmp.data.model.HostModel

class MainActivity : AppCompatActivity() {

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

        hostList = ArrayList()
        initAdapter()
        initComponents()

        binding.fab.setOnClickListener {
            hostList.add(HostModel("Host 1", "192.168.1.1"))
            adapter.notifyItemChanged(hostList.size - 1)
        }
    }

    private fun initComponents() {
    }

    private fun initAdapter() {
        adapter = HostAdapter(hostList)
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
                drawable.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP)
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

}