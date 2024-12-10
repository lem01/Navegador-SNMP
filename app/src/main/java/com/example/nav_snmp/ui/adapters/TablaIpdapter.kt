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
import com.example.nav_snmp.data.model.TablaIpModel
import com.example.nav_snmp.ui.view.ip.fragments.tabla_ip.TablaIpViewModel

class TablaIpdapter(
    private var listDataSet: MutableList<TablaIpModel>,
    val viewModel: TablaIpViewModel,
    context: Context
) :
    RecyclerView.Adapter<TablaIpdapter.ViewHolder>() {
    private val preferences: SharedPreferences =
        context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = preferences.edit()


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tableRow: TableRow
        val direccionIp: TextView
        val cascaraDeRed: TextView
        val direccionBroadcast: TextView
        val tamabioMaximoReensamblaje: TextView
        val context: Context


        init {
            tableRow = view.findViewById(R.id.tableRow)
            direccionIp = view.findViewById(R.id.tv_direccion_ip)
            cascaraDeRed = view.findViewById(R.id.tv_mascara_de_red)
            direccionBroadcast = view.findViewById(R.id.tv_direccion_broadcast)
            tamabioMaximoReensamblaje = view.findViewById(R.id.tv_tamaño_maximo_reensamblaje)
            this.context = view.context
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tabla_ip_rc_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listDataSet.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listDataSet[holder.adapterPosition]

        holder.direccionIp.text = item.direccionIp
        holder.cascaraDeRed.text = item.mascaraDered
        holder.direccionBroadcast.text = item.direccionBroadcast
        holder.tamabioMaximoReensamblaje.text = item.tamanioMaximoDeReensamblaje

    }
}
