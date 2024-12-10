package com.example.nav_snmp.ui.view.sistema.fragments.procesos_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nav_snmp.data.model.ProcesosModel
import com.example.nav_snmp.data.repository.HostRepository
import com.example.nav_snmp.databinding.FragmentProcesosBinding
import com.example.nav_snmp.ui.adapters.ProcesosAdapter
import kotlinx.coroutines.launch
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 * Use the [ProcesosFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProcesosFragment : Fragment() {
    lateinit var adapter: ProcesosAdapter
    lateinit var list: ArrayList<ProcesosModel>

    private lateinit var viewModel: ProcesosViewModel
    private var _binding: FragmentProcesosBinding? = null
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

        _binding = FragmentProcesosBinding.inflate(layoutInflater)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.cargarDatos(requireContext())
        }

        initBarraProgreso(viewModel)
        initShowDatos(viewModel)
        initAdapter()
        initObservers(viewModel)

    }

    private fun initObservers(viewModel: ProcesosViewModel) {
        viewModel.procesoModel.observe(viewLifecycleOwner) {
            list.clear()
            list.addAll(it)
            this.adapter.notifyDataSetChanged()

        }
    }

    private fun initAdapter() {
        list = ArrayList()
        adapter = ProcesosAdapter(list, viewModel, requireContext())
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun createDivider(): View {
        return View(requireContext()).apply {
            layoutParams =
                TableRow.LayoutParams(1.dp.value.toInt(), TableRow.LayoutParams.MATCH_PARENT)
            setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    android.R.color.darker_gray
                )
            )
        }
    }

    private fun initShowDatos(viewModel: ProcesosViewModel) {
        viewModel.showDatos.observe(viewLifecycleOwner) {
            if (it) {
                binding.tableLayout.visibility = View.VISIBLE

            } else {
                binding.tableLayout.visibility = View.GONE
            }
        }
    }

    private fun initBarraProgreso(viewModel: ProcesosViewModel) {
        viewModel.barraProgreso.observe(viewLifecycleOwner) {
            if (it) {
                binding.linearProgressIndicator.visibility = View.VISIBLE
            } else {
                binding.linearProgressIndicator.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null // Evita fugas de memoria

    }

    private fun initFactory() {
        val repository = HostRepository(requireContext())

        val viewModelFactory = ProcesosViewModelFactory(repository, requireContext())
        viewModel =
            ViewModelProvider(
                this,
                viewModelFactory
            )[ProcesosViewModel::class.java]
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProcesosFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProcesosFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
