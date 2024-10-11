package com.example.snmp.ui.adapters

import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.snmp.R
import com.example.snmp.data.model.HostModel

class HostAdapter(private val hostList: List<HostModel>) :
    RecyclerView.Adapter<HostAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val direccionIP: TextView
        val cardItem: CardView

        init {

            direccionIP = view.findViewById(R.id.txt_direccion_ip)
            cardItem = view.findViewById(R.id.card_view_item)
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
        val host = hostList[position]

        holder.direccionIP.text = host.direccionIP

        holder.cardItem.setOnClickListener {
            ///show popup
            val popupMenu = PopupMenu(holder.cardItem.context, holder.cardItem)


            popupMenu.menu.add(Menu.NONE, 1, Menu.NONE, "Ver")
            popupMenu.menu.add(Menu.NONE, 2, Menu.NONE, "Eliminar")
            popupMenu.menu.add(Menu.NONE, 3, Menu.NONE, "Editar")
            popupMenu.menu.add(Menu.NONE, 4, Menu.NONE, "Operación SNMP")
            popupMenu.show()

            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    1 -> {
                        //ver
                    }

                    2 -> {
                        //eliminar
                    }

                    3 -> {
                        //editar
                    }

                    4 -> {
                        //operacion snmp
                    }


                }
                true
            }

        }
    }
}