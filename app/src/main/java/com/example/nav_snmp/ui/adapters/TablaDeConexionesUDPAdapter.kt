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
import com.example.nav_snmp.data.model.TablaDeconexionesUDPModel
import com.example.nav_snmp.ui.view.udp.fragments.tabla_de_conexiones_udp_fragment.TablaDeConexionesUDPViewModel

class TablaDeConexionesUDPAdapter(
    private var listDataSet: MutableList<TablaDeconexionesUDPModel>,
    val viewModel: TablaDeConexionesUDPViewModel,
    context: Context
) :
    RecyclerView.Adapter<TablaDeConexionesUDPAdapter.ViewHolder>() {
    private val preferences: SharedPreferences =
        context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = preferences.edit()


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tableRow: TableRow
        val direccionIpLocal: TextView
        val puertoLocal: TextView

        init {
            tableRow = view.findViewById(R.id.tableRow)
            direccionIpLocal = view.findViewById(R.id.tv_direccion_local)
            puertoLocal = view.findViewById(R.id.tv_puerto_local)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tabla_de_conexiones_udp_rc_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listDataSet.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listDataSet[holder.adapterPosition]

        holder.direccionIpLocal.text = item.direccionIpLocal
        holder.puertoLocal.text = item.puertoLocal


    }
}
