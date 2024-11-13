package com.example.nav_snmp.ui.view.icmp.fragments.icmp_salida_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.nav_snmp.R
import com.example.nav_snmp.data.repository.HostRepository
import com.example.nav_snmp.databinding.FragmentIcmpEntradaBinding
import com.example.nav_snmp.databinding.FragmentIcmpSalidaBinding
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [IcmpSalidaFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class IcmpSalidaFragment : Fragment() {
    private lateinit var viewModel: IcmpSalidaViewModel
    private var _binding: FragmentIcmpSalidaBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initFactory()

        _binding = FragmentIcmpSalidaBinding.inflate(layoutInflater)
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
            binding.tvMensajesEnviados.text = it.numeroDePaquetesEnviados
            binding.tvRecibidosConError.text = it.numeroDePaquetesConError
            binding.tvMensajesConDestinoInalcanzable.text = it.mensajeConDestinoInalcanzable
            binding.tvMensajesConTiempoExedido.text = it.numeroDePaquetesConTiempoExcedido
            binding.tvMensajesConProblemasDeParametros.text =
                it.numeroDePaquetesDeProblemasDeParametros
            binding.tvControlDeFlujo.text = it.controlDeFlujo
            binding.tvNumeroDePaquetesDeRedireccion.text = it.numeroDePaquetesDeRedireccion
            binding.tvNumeroDePaquetesEco.text = it.numeroDePaqueteEco
            binding.tvNumeroDePaquetesDeMarcaDeTiempo.text = it.numeroDePaquetesMarcaTiempo

            if (it.numeroDePaquetesEnviados.isEmpty()) binding.cardMensajesEnviados.visibility =
                View.GONE
            if (it.numeroDePaquetesConError.isEmpty()) binding.cardRecibidosConError.visibility =
                View.GONE
            if (it.mensajeConDestinoInalcanzable.isEmpty()) binding.cardMensajesConDestinoInalcanzable.visibility =
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

    private fun initShowDatos(viewModel: IcmpSalidaViewModel) {
        viewModel.showDatos.observe(viewLifecycleOwner) {
            if (it) {
                binding.lyDatos.visibility = View.VISIBLE
            } else {
                binding.lyDatos.visibility = View.GONE
            }
        }
    }

    private fun initBarraProgreso(viewModel: IcmpSalidaViewModel) {
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

        val viewModelFactory = IcmpSalidaViewModelFactory(repository, requireContext())
        viewModel =
            ViewModelProvider(
                this,
                viewModelFactory
            )[IcmpSalidaViewModel::class.java]
    }
}
