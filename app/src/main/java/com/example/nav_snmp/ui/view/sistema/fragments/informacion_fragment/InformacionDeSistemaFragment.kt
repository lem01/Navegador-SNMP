package com.example.nav_snmp.ui.view.sistema.fragments.informacion_fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.nav_snmp.R
import com.example.nav_snmp.data.repository.HostRepository
import com.example.nav_snmp.databinding.FragmentInformacionDeSitemaBinding
import com.example.nav_snmp.ui.viewmodel.DescubrirHostViewModel
import com.example.nav_snmp.ui.viewmodel.DescubrirHostViewModelFactory
import com.example.nav_snmp.utils.Preferencias
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

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val ARG_PARAM1 = "param1"
    private val ARG_PARAM2 = "param2"

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

//        preferences = requireContext().getSharedPreferences("preferences", Context.MODE_PRIVATE)
//        editor = preferences.edit()
//
//        var idHost = preferences.getInt(Preferencias.ID_HOST, 0)
//        Toast.makeText(requireContext(), idHost, Toast.LENGTH_SHORT).show()
//
//        println("idHost: $idHost")

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
        // TODO: Rename and change types and number of parameters
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
//                preferences = requireContext().getSharedPreferences("preferences", Context.MODE_PRIVATE)
//        editor = preferences.edit()
//
//        var idHost = preferences.getInt(Preferencias.ID_HOST, 0)
//        Toast.makeText(requireContext(), idHost, Toast.LENGTH_SHORT).show()
//
//        println("idHost: $idHost")

//        viewModel = ViewModelProvider(this)[InformacionSistemaViewModel::class.java]
        lifecycleScope.launch {
            viewModel.cargarDatosSistema(requireContext())
        }

        initBarraProgreso(viewModel)

        viewModel.tiempoDeFuncionaiento.observe(viewLifecycleOwner) {
            binding.tvTiempoFuncionamiento.text = it
        }
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