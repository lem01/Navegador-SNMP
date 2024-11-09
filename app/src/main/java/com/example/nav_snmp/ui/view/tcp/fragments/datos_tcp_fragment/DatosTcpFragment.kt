package com.example.nav_snmp.ui.view.tcp.fragments.datos_tcp_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.nav_snmp.R
import com.example.nav_snmp.data.repository.HostRepository
import com.example.nav_snmp.databinding.FragmentDatosTcpBinding
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [DatosTcpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DatosTcpFragment : Fragment() {
    private lateinit var viewModel: DatosTcpViewModel
    private var _binding: FragmentDatosTcpBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initFactory()

        _binding = FragmentDatosTcpBinding.inflate(layoutInflater)
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

    private fun initObservers() {
        viewModel.datosTcpModel.observe(viewLifecycleOwner) {
            binding.tvTiempoMinimoDeRetransmision.text = it.tiempoMinimoDeReTransmision
            binding.tvTiempoMaximoDeRetransmision.text = it.tiempoMaximoDeReTransmision
            binding.tvMaximoDeConexionesTcp.text = it.maximoDeConexiones
            binding.tvConexionesActivasTcp.text = it.conexionesActivasIniciadas
            binding.tvConexionesPasivasTcp.text = it.conexionesPasivasEstablecidas
            binding.tvIntentosFallidosDeConexion.text = it.intentosDeConexionesFallidos
            binding.tvResetConexionesEstablecidas.text = it.reinciosDeConexiones
            binding.tvConexionesActualmenteEstablecidas.text = it.conexionesEstablecidasActuales
            binding.tvSegmentosRecibidos.text = it.segmentosRecividios
            binding.tvSegmentosEnviados.text = it.segmentosEnviados
            binding.tvSegmentosRetransmitidos.text = it.segmentosReTransmitidos
        }
    }

    private fun initShowDatos(viewModel: DatosTcpViewModel) {
        viewModel.showDatos.observe(viewLifecycleOwner) {
            if (it) {
                binding.lyDatos.visibility = View.VISIBLE
            } else {
                binding.lyDatos.visibility = View.GONE
            }
        }
    }

    private fun initBarraProgreso(viewModel: DatosTcpViewModel) {
        viewModel.barraProgreso.observe(viewLifecycleOwner) {
            if (it) {
                binding.linearProgressIndicator.visibility = View.VISIBLE
            } else {
                binding.linearProgressIndicator.visibility = View.GONE
            }
        }
    }

    private fun initFactory() {
        val repository = HostRepository(requireContext())

        val viewModelFactory = DatosTcpViewModelFactory(repository, requireContext())
        viewModel =
            ViewModelProvider(
                this,
                viewModelFactory
            )[DatosTcpViewModel::class.java]
    }
}
