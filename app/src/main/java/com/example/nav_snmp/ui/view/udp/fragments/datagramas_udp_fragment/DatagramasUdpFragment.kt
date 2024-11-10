package com.example.nav_snmp.ui.view.udp.fragments.datagramas_udp_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.nav_snmp.R
import com.example.nav_snmp.data.repository.HostRepository
import com.example.nav_snmp.databinding.FragmentDatagramasUdpBinding
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DatagramasUdpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DatagramasUdpFragment : Fragment() {
    private lateinit var viewModel: DatagramasUdpViewModel
    private var _binding: FragmentDatagramasUdpBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initFactory()

        _binding = FragmentDatagramasUdpBinding.inflate(layoutInflater)
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
        viewModel.datagramasUdpModel.observe(viewLifecycleOwner) {
            binding.tvDatagramasRecibidos.text = it.datagramasRecibidos
            binding.tvPuertosNoDisponibles.text = it.puertosNoDisponibles
            binding.tvErroresDeEntrada.text = it.erroresDeEntrada
            binding.tvDatagramasEnviados.text = it.datagramasEnviados

        }
    }

    private fun initShowDatos(viewModel: DatagramasUdpViewModel) {
        viewModel.showDatos.observe(viewLifecycleOwner) {
            if (it) {
                binding.lyDatos.visibility = View.VISIBLE
            } else {
                binding.lyDatos.visibility = View.GONE
            }
        }
    }

    private fun initBarraProgreso(viewModel: DatagramasUdpViewModel) {
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

        val viewModelFactory = DatagramasUdpViewModelFactory(repository, requireContext())
        viewModel =
            ViewModelProvider(
                this,
                viewModelFactory
            )[DatagramasUdpViewModel::class.java]
    }
}
