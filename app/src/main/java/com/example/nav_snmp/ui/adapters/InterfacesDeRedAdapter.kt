package com.example.nav_snmp.ui.adapters

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TableRow
import android.widget.TextView
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.example.nav_snmp.R
import com.example.nav_snmp.data.model.InterfacesDeRedModel
import com.example.nav_snmp.ui.view.interfaces_de_red.InterfacesDeRedViewModel
import kotlinx.coroutines.launch

class InterfacesDeRedAdapter(
    private var listDataSet: MutableList<InterfacesDeRedModel>,
    val viewModel: InterfacesDeRedViewModel,
    context: Context
) :
    RecyclerView.Adapter<InterfacesDeRedAdapter.ViewHolder>() {
    private val preferences: SharedPreferences =
        context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = preferences.edit()


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tableRow: TableRow
        val descripcion: TextView
        val tipo: TextView
        val velocidad: TextView
        val direccionMac: TextView
        val estadoOperativo: TextView
        val estadoAdministrativo: TextView
        val numeroDeBytesRecividos: TextView
        val numeroDeBytesEnviados: TextView
        val context: Context


        init {
            tableRow = view.findViewById(R.id.tableRow)
            descripcion = view.findViewById(R.id.tv_descripcion)
            tipo = view.findViewById(R.id.tv_tipo)
            velocidad = view.findViewById(R.id.tv_velcidad)
            direccionMac = view.findViewById(R.id.tv_direccion_mac)
            estadoOperativo = view.findViewById(R.id.tv_estado_operativo)
            estadoAdministrativo = view.findViewById(R.id.btn_estado_adrministrativo)
            numeroDeBytesRecividos = view.findViewById(R.id.tv_numero_de_bytes_recibidos)
            numeroDeBytesEnviados = view.findViewById(R.id.tv_numero_de_bytes_enviados)
            this.context = view.context
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.interfaces_de_red_rc_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listDataSet.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listDataSet[holder.adapterPosition]

        holder.descripcion.text = item.descripcion
        holder.tipo.text = item.tipo
        holder.velocidad.text = item.velocidad
        holder.direccionMac.text = item.direccionMac
        holder.estadoOperativo.text = item.estadoOperativo
        holder.estadoAdministrativo.text = item.estadoAdministrativo
        holder.numeroDeBytesRecividos.text = item.numeroDeBytesRecividos
        holder.numeroDeBytesEnviados.text = item.numeroDeBytesEnviados

        holder.estadoAdministrativo.setOnClickListener {
            showPopup(holder, item)
        }

    }

    private fun showPopup(holder: ViewHolder, item: InterfacesDeRedModel) {
        val popupMenu = PopupMenu(holder.context, holder.estadoAdministrativo)

        popupMenu.menu.add(Menu.NONE, 1, Menu.NONE, "Up (1)")
        popupMenu.menu.add(Menu.NONE, 2, Menu.NONE, "Down (2)")
        popupMenu.menu.add(Menu.NONE, 3, Menu.NONE, "Testing (3)")

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                1 -> {
                    viewModel.viewModelScope.launch {
                        val estadoOperativo = viewModel.setEstadoAdministrativo(item, 1)
                        holder.estadoAdministrativo.text = estadoOperativo.estadoAdministrativo
                    }
                }

                2 -> {
                    viewModel.viewModelScope.launch {
                        viewModel.setEstadoAdministrativo(item, 2)
                    }
                }

                3 -> {
                    viewModel.viewModelScope.launch {
                        viewModel.setEstadoAdministrativo(item, 3)
                    }
                }
            }
            true
        }
        popupMenu.show()

    }
}
