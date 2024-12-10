package com.example.nav_snmp.ui.view.ip.fragments.tabla_ip

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nav_snmp.data.model.TablaIpModel
import com.example.nav_snmp.data.repository.HostRepository
import com.example.nav_snmp.databinding.FragmentTablaIpBinding
import com.example.nav_snmp.ui.adapters.TablaIpdapter

import kotlinx.coroutines.launch


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TablaIpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TablaIpFragment : Fragment() {

    lateinit var adapter: TablaIpdapter
    lateinit var list: ArrayList<TablaIpModel>
    
    private lateinit var viewModel: TablaIpViewModel
    private var _binding: FragmentTablaIpBinding? = null
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
    ): View? {
        initFactory()

        _binding = FragmentTablaIpBinding.inflate(layoutInflater)
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

    private fun initAdapter() {
        list = ArrayList()
        adapter = TablaIpdapter(list, viewModel, requireContext())
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun initObservers(viewModel: TablaIpViewModel) {
        viewModel.tablaIpModel.observe(viewLifecycleOwner) {
            list.clear()
            list.addAll(it)
            this.adapter.notifyDataSetChanged()
            Log.d("TablaIp", "initObservers: $list")
        }
    }

    private fun initShowDatos(viewModel: TablaIpViewModel) {
        viewModel.showDatos.observe(viewLifecycleOwner) {
            if (it) {
                binding.tableLayout.visibility = View.VISIBLE

            } else {
                binding.tableLayout.visibility = View.GONE

            }
        }
    }

    private fun initBarraProgreso(viewModel: TablaIpViewModel) {
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

        val viewModelFactory =
            TablaIpViewModelFactory(repository, requireContext())
        viewModel =
            ViewModelProvider(
                this,
                viewModelFactory
            )[TablaIpViewModel::class.java]
    }

    override fun onDestroyView() {
        _binding = null // Evita fugas de memoria
        super.onDestroyView()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TablaIpFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TablaIpFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
