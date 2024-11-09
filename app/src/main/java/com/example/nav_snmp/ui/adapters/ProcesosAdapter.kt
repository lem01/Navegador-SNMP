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
import com.example.nav_snmp.data.model.ProcesosModel
import com.example.nav_snmp.ui.view.sistema.fragments.procesos_fragment.ProcesosViewModel

class ProcesosAdapter(
    private var listDataSet: MutableList<ProcesosModel>,
    val viewModel: ProcesosViewModel,
    context: Context
) :
    RecyclerView.Adapter<ProcesosAdapter.ViewHolder>() {
    private val preferences: SharedPreferences =
        context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = preferences.edit()


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tableRow: TableRow
        val indice: TextView
        val estado: TextView
        val descripcion: TextView
        val errores: TextView
        val context: Context


        init {
            tableRow = view.findViewById(R.id.tableRow)
            indice = view.findViewById(R.id.tv_indice)
            descripcion = view.findViewById(R.id.tv_descipcion)
            estado = view.findViewById(R.id.tv_estado)
            errores = view.findViewById(R.id.tv_errores)
            this.context = view.context
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.procesos_rc_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listDataSet.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listDataSet[holder.adapterPosition]

        holder.indice.text = item.indice
        holder.descripcion.text = item.descripcion
        holder.estado.text = item.estado
        holder.errores.text = item.errores


    }
}
