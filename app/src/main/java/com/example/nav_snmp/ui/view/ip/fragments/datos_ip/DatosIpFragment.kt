package com.example.nav_snmp.ui.view.ip.fragments.datos_ip

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.nav_snmp.R
import com.example.nav_snmp.data.repository.HostRepository
import com.example.nav_snmp.databinding.FragmentDatosIPBinding
import com.example.nav_snmp.utils.CommonOids
import kotlinx.coroutines.launch

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DatosIpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DatosIpFragment : Fragment() {
    private lateinit var viewModel: DatosIpViewModel
    private var _binding: FragmentDatosIPBinding? = null
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

        _binding = FragmentDatosIPBinding.inflate(layoutInflater)
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

        editables()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }

    private fun editables() {
        binding.cardEstadoReenvioPaquetes.setOnClickListener {
            dialogoEditarNombre(
                "Editar",
                CommonOids.IP.ipForwarding,
                viewModel.datosIpModel.value?.estadoReenvioPaquetes
            )
        }

        binding.cardTtlPredeterminado.setOnClickListener {
            dialogoEditarNombre(
                "Editar",
                CommonOids.IP.ipDefaultTTL,
                viewModel.datosIpModel.value?.ttlPredeterminado
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

    private fun initObservers() {
        viewModel.datosIpModel.observe(viewLifecycleOwner) {
            binding.tvEstadoReenvioPaquetes.text = it.estadoReenvioPaquetes
            binding.tvTtlPredeterminado.text = it.ttlPredeterminado
            binding.tvTotalDatagramasIpRecibidos.text = it.totalDatagramasIpRecibidos
            binding.tvErroresCabecera.text = it.erroresCabecera
            binding.tvErroresDireccion.text = it.erroresDireccion
            binding.tvDatagramasIpReenviados.text = it.datagramasIpReenviados
            binding.tvConProtocolosDesconocidos.text = it.datagranasConProtcolosDesconocidos
            binding.tvDescartadosSinError.text = it.descartadosSinError
            binding.tvEntregadosCorrectamente.text = it.entregadosCorrectamente
            binding.tvGeneradosParaEnvio.text = it.generadosParaEnvio
            binding.tvDescartadosAlEnviar.text = it.descartadosAlEnviar
            binding.tvSinRutaDisponible.text = it.sinRutaDisponible
            binding.tvTiempoDeEsperaReensamblajeDeFragmentos.text =
                it.tiempoDesEsperaReensamblajeDeFragmentos
            binding.tvSolicitudesReensamblajeFragmentos.text =
                it.solicitudesReensambleajeDeFragmentos
            binding.tvReensamblajesExitosos.text = it.reensamblajeExitosos
            binding.tvFallosEnReensamblajeDatagramasIp.text = it.fallosEnElReensamblajeDatagramasIP
            binding.tvFragmentacionExitosa.text = it.fragmentacionExitosaDatagramasIp
            binding.tvFallosEnLaFragmentacionDatagramasIp.text =
                it.fallosEnlaFragmentacionDeDatagramasIp
            binding.tvfragmentosIpGeneradosPorFragmentacion.text =
                it.fragmentosGeneradosPorFragmentacion
            binding.tvEntradasDeEnrutamientosDescartadas.text = it.entradasEnrutamientoDescartadas

            if (it.estadoReenvioPaquetes.isEmpty()) binding.constraintEstadoReenvioPaquetes.visibility =
                View.GONE
            if (it.ttlPredeterminado.isEmpty()) binding.constraintTtlPredeterminado.visibility =
                View.GONE
            if (it.totalDatagramasIpRecibidos.isEmpty()) binding.cardTotalDatagramasIpRecibidos.visibility =
                View.GONE
            if (it.erroresCabecera.isEmpty()) binding.cardErroresCabecera.visibility =
                View.GONE
            if (it.erroresDireccion.isEmpty()) binding.cardErroresDireccion.visibility =
                View.GONE
            if (it.datagramasIpReenviados.isEmpty()) binding.cardDatagramasIpReenviados.visibility =
                View.GONE
            if (it.datagranasConProtcolosDesconocidos.isEmpty()) binding.cardConProtocolosDesconocidos.visibility =
                View.GONE
            if (it.descartadosSinError.isEmpty()) binding.cardDescartadosSinError.visibility =
                View.GONE
            if (it.entregadosCorrectamente.isEmpty()) binding.cardEntregadosCorrectamente.visibility =
                View.GONE
            if (it.generadosParaEnvio.isEmpty()) binding.cardGeneradosParaEnvio.visibility =
                View.GONE
            if (it.descartadosAlEnviar.isEmpty()) binding.cardDescartadosAlEnviar.visibility =
                View.GONE
            if (it.sinRutaDisponible.isEmpty()) binding.cardSinRutaDisponible.visibility =
                View.GONE
            if (it.tiempoDesEsperaReensamblajeDeFragmentos.isEmpty()) binding.cardTiempoDeEsperaReensamblajeDeFragmentos.visibility =
                View.GONE
            if (it.solicitudesReensambleajeDeFragmentos.isEmpty()) binding.cardSolicitudesReensamblajeFragmentos.visibility =
                View.GONE
            if (it.reensamblajeExitosos.isEmpty()) binding.cardReensamblajesExitosos.visibility =
                View.GONE
            if (it.fallosEnElReensamblajeDatagramasIP.isEmpty()) binding.cardFallosEnReensamblajeDatagramasIp.visibility =
                View.GONE
            if (it.fragmentacionExitosaDatagramasIp.isEmpty()) binding.cardFragmentacionExitosa.visibility =
                View.GONE
            if (it.fallosEnlaFragmentacionDeDatagramasIp.isEmpty()) binding.cardFallosEnLaFragmentacionDatagramasIp.visibility =
                View.GONE
            if (it.fragmentosGeneradosPorFragmentacion.isEmpty()) binding.cardfragmentosIpGeneradosPorFragmentacion.visibility =
                View.GONE
            if (it.entradasEnrutamientoDescartadas.isEmpty()) binding.cardEntradasDeEnrutamientosDescartadas.visibility =
                View.GONE

        }
    }

    private fun initShowDatos(viewModel: DatosIpViewModel) {
        viewModel.showDatos.observe(viewLifecycleOwner) {
            if (it) {
                binding.lyDatos.visibility = View.VISIBLE
            } else {
                binding.lyDatos.visibility = View.GONE
            }
        }
    }

    private fun initBarraProgreso(viewModel: DatosIpViewModel) {
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

        val viewModelFactory = DatosIpViewModelFactory(repository, requireContext())
        viewModel =
            ViewModelProvider(
                this,
                viewModelFactory
            )[DatosIpViewModel::class.java]
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DatosIPFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DatosIpFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
