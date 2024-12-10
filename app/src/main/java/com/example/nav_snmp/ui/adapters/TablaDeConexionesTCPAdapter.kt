package com.example.nav_snmp.ui.adapters

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nav_snmp.R
import com.example.nav_snmp.data.model.TablaDeConexionesTCPModel
import com.example.nav_snmp.ui.view.tcp.fragments.tabla_de_conexiones_fragment.TablaDeConexionesTCPViewModel

class TablaDeConexionesTCPAdapter(
    private var listDataSet: MutableList<TablaDeConexionesTCPModel>,
    val viewModel: TablaDeConexionesTCPViewModel,
    context: Context
) :
    RecyclerView.Adapter<TablaDeConexionesTCPAdapter.ViewHolder>() {
    private val preferences: SharedPreferences =
        context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = preferences.edit()


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tableRow: TableRow
        val estadoActual: TextView
        val direccionIpLocal: TextView
        val puertoLocal: TextView
        val direccionIpRemota: TextView
        val puertoRemoto: TextView
        val context: Context


        init {
            tableRow = view.findViewById(R.id.tableRow)
            estadoActual = view.findViewById(R.id.tv_estado_actual)
            direccionIpLocal = view.findViewById(R.id.tv_direccion_ip_local)
            puertoLocal = view.findViewById(R.id.tv_puerto_local)
            direccionIpRemota = view.findViewById(R.id.tv_direccion_ip_remota)
            puertoRemoto = view.findViewById(R.id.tv_puerto_remoto)
            this.context = view.context
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tabla_de_conexiones_tcp_rc_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listDataSet.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listDataSet[holder.adapterPosition]

        holder.estadoActual.text = item.estadoActual
        holder.direccionIpLocal.text = item.direccionIpLocal
        holder.puertoLocal.text = item.puertoLocal
        holder.direccionIpRemota.text = item.direccionIpRemota
        holder.puertoRemoto.text = item.puertoRemoto


    }
}
