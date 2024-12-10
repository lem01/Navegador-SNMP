package com.example.nav_snmp.ui.view.icmp.fragments.icmp_entrada_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.nav_snmp.data.repository.HostRepository
import com.example.nav_snmp.databinding.FragmentIcmpEntradaBinding
import kotlinx.coroutines.launch

class IcmpEntradaFragment : Fragment() {

    private lateinit var viewModel: IcmpEntradaViewModel
    private var _binding: FragmentIcmpEntradaBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initFactory()

        _binding = FragmentIcmpEntradaBinding.inflate(layoutInflater)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.cargarDatosSistema(requireContext())
        }

        initObservers()

        initBarraProgreso(viewModel)
        initShowDatos(viewModel)
    }

    private fun initObservers() {
        viewModel.icmpModel.observe(viewLifecycleOwner) {
            binding.tvMensajesRecibidos.text = it.numeroDePaquetesRecibidos
            binding.tvRecibidosConError.text = it.numeroDePaquetesConError
            binding.tvMensajesConDestinoInalcanzable.text = it.mensejeConDestinoInalcanzable
            binding.tvMensajesConTiempoExedido.text = it.numeroDePaquetesConTiempoExcedido
            binding.tvMensajesConProblemasDeParametros.text =
                it.numeroDePaquetesDeProblemasDeParametros
            binding.tvControlDeFlujo.text = it.controlDeFlujo
            binding.tvNumeroDePaquetesDeRedireccion.text = it.numeroDePaquetesDeRedireccion
            binding.tvNumeroDePaquetesEco.text = it.numeroDePaqueteEco
            binding.tvNumeroDePaquetesDeMarcaDeTiempo.text = it.numeroDePaquetesMarcaTiempo

            if (it.numeroDePaquetesRecibidos.isEmpty()) binding.cardMensajesRecibidos.visibility =
                View.GONE
            if (it.numeroDePaquetesConError.isEmpty()) binding.cardRecibidosConError.visibility =
                View.GONE
            if (it.mensejeConDestinoInalcanzable.isEmpty()) binding.cardMensajesConDestinoInalcanzable.visibility =
                View.GONE
            if (it.numeroDePaquetesConTiempoExcedido.isEmpty()) binding.cardMensajesConTiempoExedido.visibility =
                View.GONE
            if (it.numeroDePaquetesDeProblemasDeParametros.isEmpty()) binding.cardMensajesConProblemasDeParametros.visibility =
                View.GONE
            if (it.controlDeFlujo.isEmpty()) binding.cardControlDeFlujo.visibility = View.GONE
            if (it.numeroDePaquetesDeRedireccion.isEmpty()) binding.cardNumeroDePaquetesDeRedireccion.visibility =
                View.GONE
            if (it.numeroDePaqueteEco.isEmpty()) binding.cardNumeroDePaquetesEco.visibility =
                View.GONE
            if (it.numeroDePaquetesMarcaTiempo.isEmpty()) binding.cardNumeroDePaquetesDeMarcaDeTiempo.visibility =
                View.GONE

        }
    }

    private fun initShowDatos(viewModel: IcmpEntradaViewModel) {
        viewModel.showDatos.observe(viewLifecycleOwner) {
            if (it) {
                binding.lyDatos.visibility = View.VISIBLE
            } else {
                binding.lyDatos.visibility = View.GONE
            }
        }
    }

    private fun initBarraProgreso(viewModel: IcmpEntradaViewModel) {
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

        val viewModelFactory = IcmpEntradaViewModelFactory(repository, requireContext())
        viewModel =
            ViewModelProvider(
                this,
                viewModelFactory
            )[IcmpEntradaViewModel::class.java]
    }
}
