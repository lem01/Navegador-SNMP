package com.example.nav_snmp.ui.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.nav_snmp.R
import com.example.nav_snmp.data.model.HostModel
import com.example.nav_snmp.ui.view.IpDetallesActivity
import com.example.nav_snmp.ui.viewmodel.HostViewModel
import com.example.nav_snmp.utils.TipoDispositivo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HostAdapter(private var hostList: MutableList<HostModel>, val hostViewModel: HostViewModel) :
    RecyclerView.Adapter<HostAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val direccionIP: TextView
        val cardItem: CardView
        val imgHost: ImageView

        init {

            direccionIP = view.findViewById(R.id.txt_direccion_ip)
            cardItem = view.findViewById(R.id.card_view_item)
            imgHost = view.findViewById(R.id.img_host)
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
        val host = hostList[holder.adapterPosition]

        initImgHost(host, holder)

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

                        val intent =
                            Intent(holder.cardItem.context, IpDetallesActivity::class.java).apply {
                                putExtra("idHost", host.id)
                                putExtra("verHost", true)
                            }
                        startActivity(holder.cardItem.context, intent, null)
                    }

                    2 -> {

                        hostViewModel.detelteHost(host.id)
                        //eliminar
                        hostList.removeAt(holder.adapterPosition)
                        notifyItemRemoved(holder.adapterPosition)

                    }

                    3 -> {

                        //iniciar nueva actividad y pasar el host
                        val intent =
                            Intent(holder.cardItem.context, IpDetallesActivity::class.java).apply {
                                putExtra("idHost", host.id)
                                putExtra("editarHost", true)
                            }
                        startActivity(holder.cardItem.context, intent, null)

                    }

                    4 -> {
                        //operacion snmp
                    }


                }
                true
            }

        }
    }

    private fun initImgHost(host: HostModel, holder: ViewHolder) {
        holder.imgHost.setImageResource(
            when (host.tipoDeDispositivo) {
                TipoDispositivo.ROUTER.toString() -> R.drawable.router
                TipoDispositivo.IMPRESORA.toString() -> R.drawable.printer
                TipoDispositivo.SERVIDOR.toString() -> R.drawable.server
                TipoDispositivo.SWITCH.toString() -> R.drawable.network_switch
                else -> R.drawable.host
            }
        )
    }


}
