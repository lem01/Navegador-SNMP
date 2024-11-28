package com.example.nav_snmp.ui.view.sistema.fragments.informacion_fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.nav_snmp.R
import com.example.nav_snmp.data.repository.HostRepository
import com.example.nav_snmp.databinding.FragmentInformacionDeSitemaBinding
import com.example.nav_snmp.utils.CommonOids
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [InformacionDeSistemaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InformacionDeSistemaFragment : Fragment() {
    private lateinit var viewModel: InformacionSistemaViewModel
    private var _binding: FragmentInformacionDeSitemaBinding? = null
    private val binding get() = _binding!!

    private var param1: String? = null
    private var param2: String? = null
    private val ARG_PARAM1 = "param1"
    private val ARG_PARAM2 = "param2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        initFactory()

        _binding = FragmentInformacionDeSitemaBinding.inflate(layoutInflater)
        val root: View = binding.root

        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InformacionDeSitemaFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InformacionDeSistemaFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.cargarDatosSistema(requireContext())
        }

        viewModel.sistemaModel.observe(viewLifecycleOwner) { sistemaModel ->
            binding.tvTiempoFuncionamiento.text = sistemaModel?.tiempoDeFuncionamiento
            binding.tvFecha.text = sistemaModel?.fecha
            binding.tvNoUsuarios.text = sistemaModel?.numeroDeUsuarios
            binding.tvNoProcesos.text = sistemaModel?.numeroDeProcesos
            binding.tvMaxProcesos.text = sistemaModel?.maximoNumeroDeProcesos
            binding.tvNombre.text = sistemaModel?.nombre
            binding.tvDescripcion.text = sistemaModel?.descripcion
            binding.tvLocalizacion.text = sistemaModel?.localizacion
            binding.tvContacto.text = sistemaModel?.contacto

            if (sistemaModel.fecha == "") binding.lyFecha.visibility = View.GONE
            if (sistemaModel.numeroDeUsuarios == "") binding.lyNoUsuarios.visibility = View.GONE
            if (sistemaModel.numeroDeProcesos == "") binding.lyNoProcesos.visibility = View.GONE
            if (sistemaModel.maximoNumeroDeProcesos == "") binding.lyMaxProcesos.visibility =
                View.GONE
            if (sistemaModel.descripcion == "") binding.lyDescripcion.visibility = View.GONE
            if (sistemaModel.tiempoDeFuncionamiento == "") binding.lyTiempoFuncionamiento.visibility =
                View.GONE

        }

        initBarraProgreso(viewModel)
        initShowDatos(viewModel)
        editables()
    }

    private fun editables() {
        binding.cardNombre.setOnClickListener {
            dialogoEditarNombre(
                "Editar Nombre",
                CommonOids.SYSTEM.SYS_NAME,
                viewModel.sistemaModel.value?.nombre
            )
        }
        binding.cardCotacto.setOnClickListener {
            dialogoEditarNombre(
                "Editar Contacto",
                CommonOids.SYSTEM.SYS_CONTACT,
                viewModel.sistemaModel.value?.contacto
            )
        }
        binding.cardLocalizacion.setOnClickListener {
            dialogoEditarNombre(
                "Editar Localización",
                CommonOids.SYSTEM.SYS_LOCATION,
                viewModel.sistemaModel.value?.localizacion
            )
        }

    }

    private fun dialogoEditarNombre(s: String, oid: String, valor: String?) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_editar_datos_simples)
        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()

        val etDialog = dialog.findViewById<TextView>(R.id.et_dialogo)
        etDialog.text = valor

        dialog.findViewById<View>(R.id.btn_cancelar).setOnClickListener {
            dialog.dismiss()
        }
        dialog.findViewById<View>(R.id.btn_aceptar).setOnClickListener {
            val valorNuevo = etDialog.text.toString()

            lifecycleScope.launch {
                viewModel.editarDatos(oid, valorNuevo)
                dialog.dismiss()
            }
        }

        dialog.findViewById<TextView>(R.id.txt_tittle).text = s

    }

    private fun initShowDatos(viewModel: InformacionSistemaViewModel) {
        viewModel.showInformacionSistema.observe(viewLifecycleOwner) {
            if (it) {
                binding.lyInformacionDeSistema.visibility = View.VISIBLE
            } else {
                binding.lyInformacionDeSistema.visibility = View.GONE
            }
        }
    }

    private fun initBarraProgreso(viewModel: InformacionSistemaViewModel) {
        viewModel.barraProgreso.observe(viewLifecycleOwner) {
            if (it) {
                binding.linearProgressIndicator.visibility = View.VISIBLE
            } else {
                binding.linearProgressIndicator.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Evita fugas de memoria
    }


    private fun initFactory() {
        val repository = HostRepository(requireContext())

        val viewModelFactory = InformacionSistemaViewModelFactory(repository, requireContext())
        viewModel =
            ViewModelProvider(
                this,
                viewModelFactory
            )[InformacionSistemaViewModel::class.java]
    }
}
