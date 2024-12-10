package com.example.nav_snmp.ui.view.snmp.fragments.recibidos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.nav_snmp.R
import com.example.nav_snmp.data.repository.HostRepository
import com.example.nav_snmp.databinding.FragmentInformacionDeSitemaBinding
import com.example.nav_snmp.databinding.FragmentSnmpRecibidosBinding
import com.example.nav_snmp.ui.view.sistema.fragments.informacion_fragment.InformacionSistemaViewModel
import com.example.nav_snmp.ui.view.sistema.fragments.informacion_fragment.InformacionSistemaViewModelFactory
import com.example.nav_snmp.ui.view.udp.fragments.datagramas_udp_fragment.DatagramasUdpViewModel
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SnmpRecibidosFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SnmpRecibidosFragment : Fragment() {
    private lateinit var viewModel: SnmpRecibidosViewModel
    private var _binding: FragmentSnmpRecibidosBinding? = null
    private val binding get() = _binding!!

    private var param1: String? = null
    private var param2: String? = null

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

        _binding = FragmentSnmpRecibidosBinding.inflate(layoutInflater)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.cargarDatos(requireContext())
        }

        initObservers()


        initBarraProgreso(viewModel)
        initShowDatos(viewModel)
    }
    private fun initShowDatos(viewModel: SnmpRecibidosViewModel) {
        viewModel.showDatos.observe(viewLifecycleOwner) {
            if (it) {
                binding.lyDatos.visibility = View.VISIBLE
            } else {
                binding.lyDatos.visibility = View.GONE
            }
        }
    }
    private fun initBarraProgreso(viewModel: SnmpRecibidosViewModel) {
        viewModel.barraProgreso.observe(viewLifecycleOwner) {
            if (it) {
                binding.linearProgressIndicator.visibility = View.VISIBLE
            } else {
                binding.linearProgressIndicator.visibility = View.GONE
            }
        }
    }


    private fun initObservers() {
        viewModel.snmpRecibidosModel.observe(viewLifecycleOwner) {
            binding.tvVariablesSolicitudesRecibidasGetSet.text =
                it.variablesSolicitudesGetSetRecibidas
            binding.tvVariablesSolicitudesRecibidasSet.text = it.variablesSolicitudesSetRecibidas
            binding.tvSolicitudesGetRecibidas.text = it.solicitudesGetRecibidas
            binding.tvSolicitudesGetNextRecibidas.text = it.solicitudesGetNextRecibidas
            binding.tvSolicitudesSetRecibidas.text = it.solicitudesSetRecibidas
            binding.tvRespuestasGetRecibidas.text = it.respuestasGetRecibidas
            binding.tvMensajesTrapSnmpRecibidos.text = it.mensajesTrapSnmpRecibidos

            binding.tvVersionSnmpNoSoportada.text = it.conVersionSnmpNoSoportada
            binding.tvNombreComunidadSnmpInavalida.text = it.nombreComunidadSnmpInvalido
            binding.tvErroresDeSintaxisAsn1.text = it.erroresDeSintaxisAsn1
            binding.tvDemasiadoParaCaberEnUnSoloMensaje.text = it.grandeParaCaberEnUnMensaje
            binding.tvObjetoSolicitadoNoExiste.text = it.objetoSolicitadoNoExiste
            binding.tvSolicitudesDeEscrituraParaObjetosDeSoloLectura.text =
                it.solicitudesDeEscrituraAObjetosDeLectura
            binding.tvErroresGenericosNoEspecificados.text = it.erroresGenericosNoEspecificados

            if (it.variablesSolicitudesGetSetRecibidas.isEmpty()) binding.lyVariablesSolicitudesRecibidasGetSet.visibility =
                View.GONE
            if (it.variablesSolicitudesSetRecibidas.isEmpty()) binding.lyVariablesSolicitudesRecibidasSet.visibility =
                View.GONE
            if (it.solicitudesGetRecibidas.isEmpty()) binding.lySolicitudesGetRecibidas.visibility =
                View.GONE
            if (it.solicitudesGetNextRecibidas.isEmpty()) binding.lySolicitudesGetNextRecibidas.visibility =
                View.GONE
            if (it.solicitudesSetRecibidas.isEmpty()) binding.lySolicitudesSetRecibidas.visibility =
                View.GONE
            if (it.respuestasGetRecibidas.isEmpty()) binding.lyRespuestasGetRecibidas.visibility =
                View.GONE
            if (it.mensajesTrapSnmpRecibidos.isEmpty()) binding.lyMensajesTrapSnmpRecibidos.visibility =
                View.GONE

            if (it.conVersionSnmpNoSoportada.isEmpty()) binding.lyVersionSnmpNoSoportada.visibility =
                View.GONE
            if (it.nombreComunidadSnmpInvalido.isEmpty()) binding.lyNombreComunidadSnmpInavalida.visibility =
                View.GONE
            if (it.conUsoInapropiadoDeComunidad.isEmpty()) binding.cardUsoInapropiadoComunidad.visibility =
                View.GONE
            if (it.erroresDeSintaxisAsn1.isEmpty()) binding.cardErroresDeSintaxisAsn1.visibility =
                View.GONE
            if (it.grandeParaCaberEnUnMensaje.isEmpty()) binding.cardDemasiadoParaCaberEnUnSoloMensaje.visibility =
                View.GONE
            if (it.objetoSolicitadoNoExiste.isEmpty()) binding.cardObjetoSolicitadoNoExiste.visibility =
                View.GONE
            if (it.solicitudesDeEscrituraAObjetosDeLectura.isEmpty())
                binding.cardSolicitudesDeEscrituraParaObjetosDeSoloLectura.visibility = View.GONE
            if (it.erroresGenericosNoEspecificados.isEmpty())
                binding.cardErroresGenericosNoEspecificados.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Evita fugas de memoria
    }

    private fun initFactory() {
        val repository = HostRepository(requireContext())

        val viewModelFactory =
            SnmpRecibidosViewModel.SnmpRecibidosViewModelFactory(repository, requireContext())
        viewModel =
            ViewModelProvider(
                this,
                viewModelFactory
            )[SnmpRecibidosViewModel::class.java]
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RecibidosFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SnmpRecibidosFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
