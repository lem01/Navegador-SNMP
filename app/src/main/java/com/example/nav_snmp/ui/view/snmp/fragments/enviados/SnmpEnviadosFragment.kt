package com.example.nav_snmp.ui.view.snmp.fragments.enviados

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
import com.example.nav_snmp.databinding.FragmentSnmpEnviadosBinding
import com.example.nav_snmp.ui.view.sistema.fragments.informacion_fragment.InformacionSistemaViewModel
import com.example.nav_snmp.ui.view.sistema.fragments.informacion_fragment.InformacionSistemaViewModelFactory
import com.example.nav_snmp.ui.view.snmp.fragments.enviados.SnmpEnviadosViewModel
import com.example.nav_snmp.ui.view.udp.fragments.datagramas_udp_fragment.DatagramasUdpViewModel
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SnmpEnviadosFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SnmpEnviadosFragment : Fragment() {
    private lateinit var viewModel: SnmpEnviadosViewModel
    private var _binding: FragmentSnmpEnviadosBinding? = null
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

        _binding = FragmentSnmpEnviadosBinding.inflate(layoutInflater)
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

    private fun initShowDatos(viewModel: SnmpEnviadosViewModel) {
        viewModel.showDatos.observe(viewLifecycleOwner) {
            if (it) {
                binding.lyDatos.visibility = View.VISIBLE
            } else {
                binding.lyDatos.visibility = View.GONE
            }
        }
    }

    private fun initBarraProgreso(viewModel: SnmpEnviadosViewModel) {
        viewModel.barraProgreso.observe(viewLifecycleOwner) {
            if (it) {
                binding.linearProgressIndicator.visibility = View.VISIBLE
            } else {
                binding.linearProgressIndicator.visibility = View.GONE
            }
        }
    }

    private fun initObservers() {
        viewModel.SnmpEnviadosModel.observe(viewLifecycleOwner) {
            binding.tvTotalPaquetesSnmpEnviados.text =
                it.totalPaquetesSnmpEnviados
            binding.tvSolicitudesGetEnviadas.text = it.solicitudesGetEnviadas
            binding.tvSolicitudesGetNextEnviadas.text = it.solicitudesGetNextEnviadas
            binding.tvSolicitudesSetEnviadas.text = it.solicitudesSetEnviadas
            binding.tvRespuestasGetEnviadas.text = it.respuestasGetEnviadas
            binding.tvTrapsSnmpEnviados.text = it.trapsSnmpEnviados

            binding.tvRespuestaDemasiadoGrande.text = it.respuestaEsDemasiadoGrande
            binding.tvObjetoSolicitadoNoExiste.text = it.objetoSolicitadoNoExiste
            binding.tvInvalidosEnOperacionesSet.text = it.invalidosEnOperacionesSet
            binding.tvErrorresGenericosNoEspecificados.text = it.erroresGenericosNoEspecificados

            if (it.totalPaquetesSnmpEnviados.isEmpty())
                binding.constraintTotalPaquetesSnmpEnviados.visibility = View.GONE
            if (it.solicitudesGetEnviadas.isEmpty())
                binding.constraintSolicitudesGetEnviadas.visibility = View.GONE
            if (it.solicitudesGetNextEnviadas.isEmpty())
                binding.constraintSolicitudesGetNextEnviadas.visibility = View.GONE

            if (it.solicitudesSetEnviadas.isEmpty())
                binding.cardSolicitudesSetEnviadas.visibility = View.GONE
            if (it.respuestasGetEnviadas.isEmpty())
                binding.cardRespuestasGetEnviadas.visibility = View.GONE
            if (it.trapsSnmpEnviados.isEmpty())
                binding.cardTrapsSnmpEnviados.visibility = View.GONE

            if (it.respuestaEsDemasiadoGrande.isEmpty())
                binding.constraintRespuestaDemasiadoGrande.visibility = View.GONE
            if (it.objetoSolicitadoNoExiste.isEmpty())
                binding.constraintdObjetoSolicitadoNoExiste.visibility = View.GONE
            if (it.invalidosEnOperacionesSet.isEmpty())
                binding.constraintInvalidosEnOperacionesSet.visibility = View.GONE
            if (it.erroresGenericosNoEspecificados.isEmpty())
                binding.constraintErrorresGenericosNoEspecificados.visibility = View.GONE

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Evita fugas de memoria
    }

    private fun initFactory() {
        val repository = HostRepository(requireContext())

        val viewModelFactory =
            SnmpEnviadosViewModel.SnmpEnviadosViewModelFactory(repository, requireContext())
        viewModel =
            ViewModelProvider(
                this,
                viewModelFactory
            )[SnmpEnviadosViewModel::class.java]
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EnviadosFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SnmpEnviadosFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
