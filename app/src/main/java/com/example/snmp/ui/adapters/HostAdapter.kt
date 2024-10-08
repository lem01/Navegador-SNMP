package com.example.snmp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.snmp.R
import com.example.snmp.data.model.HostModel

class HostAdapter(private val hostList: List<HostModel>) :
    RecyclerView.Adapter<HostAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombreHost: TextView
        val direccionIP: TextView

        init {
            nombreHost = view.findViewById(R.id.txt_nombre_host)
            direccionIP = view.findViewById(R.id.txt_direccion_ip)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rc_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return hostList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.nombreHost.text = hostList[position].nombreHost
        holder.direccionIP.text = hostList[position].direccionIP
    }
}