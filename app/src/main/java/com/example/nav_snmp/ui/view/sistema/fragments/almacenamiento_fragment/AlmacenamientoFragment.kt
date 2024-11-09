package com.example.nav_snmp.ui.view.sistema.fragments.almacenamiento_fragment

import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.nav_snmp.data.repository.HostRepository
import com.example.nav_snmp.databinding.FragmentAlmacenamientoBinding
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [AlmacenamientoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlmacenamientoFragment : Fragment() {

    private lateinit var viewModel: AlmacenamientoViewModel
    private var _binding: FragmentAlmacenamientoBinding? = null
    private val binding get() = _binding!!

    // TODO: Rename and change types of parameters
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

        _binding = FragmentAlmacenamientoBinding.inflate(layoutInflater)
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

        initTableLayout(viewModel)
    }

    private fun initTableLayout(viewModel: AlmacenamientoViewModel) {

        viewModel.almacenamientoModel.observe(viewLifecycleOwner) {

            val headerRow = TableRow(requireContext()).apply {
                showDividers = TableRow.SHOW_DIVIDER_MIDDLE
                dividerDrawable =
                    resources.getDrawable(android.R.drawable.divider_horizontal_bright, null)

                val headers = listOf("Nombre", "Tamaño", "Usado", "Libre")
                headers.forEach { header ->
                    addView(TextView(requireContext()).apply {
                        text = header
                        layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                        gravity = Gravity.CENTER
                        setPadding(
                            5.dp.value.toInt(),
                            5.dp.value.toInt(),
                            5.dp.value.toInt(),
                            8.dp.value.toInt()
                        )
                        setTypeface(typeface, Typeface.BOLD)
                        setTextSize(16f)
                    })
                }
            }
            binding.tableLayout.addView(headerRow)

            it.map {
                val tableRow = TableRow(requireContext()).apply {
                    showDividers = TableRow.SHOW_DIVIDER_MIDDLE
                    dividerDrawable =
                        resources.getDrawable(android.R.drawable.divider_horizontal_bright)
                }
                val textView = TextView(requireContext()).apply {
                    text = it.nombre
                    layoutParams =
                        TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)  // Peso
                    gravity = Gravity.CENTER
                    setPadding(
                        5.dp.value.toInt(),
                        5.dp.value.toInt(),
                        5.dp.value.toInt(),
                        8.dp.value.toInt()
                    )
                }

                val textView2 = TextView(requireContext()).apply {
                    text = it.tamaño
                    layoutParams =
                        TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)  // Peso
                    gravity = Gravity.CENTER
                    setPadding(
                        5.dp.value.toInt(),
                        5.dp.value.toInt(),
                        5.dp.value.toInt(),
                        8.dp.value.toInt()
                    )
                }
                val textView3 = TextView(requireContext()).apply {
                    text = it.usado
                    layoutParams =
                        TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)  // Peso
                    gravity = Gravity.CENTER
                    setPadding(
                        5.dp.value.toInt(),
                        5.dp.value.toInt(),
                        5.dp.value.toInt(),
                        8.dp.value.toInt()
                    )
                }

                val textView4 = TextView(requireContext()).apply {
                    text = it.libre
                    layoutParams =
                        TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)  // Peso
                    gravity = Gravity.CENTER
                    setPadding(
                        5.dp.value.toInt(),
                        5.dp.value.toInt(),
                        5.dp.value.toInt(),
                        8.dp.value.toInt()
                    )
                }

                tableRow.addView(textView)
                tableRow.addView(textView2)
                tableRow.addView(textView3)
                tableRow.addView(textView4)
                binding.tableLayout.addView(tableRow)
            }

        }
    }

    private fun initShowDatos(viewModel: AlmacenamientoViewModel) {
        viewModel.showDatos.observe(viewLifecycleOwner) {
            if (it) {
                binding.tableLayout.visibility = View.VISIBLE

            } else {
                binding.tableLayout.visibility = View.GONE
            }
        }
    }

    private fun initBarraProgreso(viewModel: AlmacenamientoViewModel) {
        viewModel.barraProgreso.observe(viewLifecycleOwner) {
            if (it) {
                binding.linearProgressIndicator.visibility = View.VISIBLE
            } else {
                binding.linearProgressIndicator.visibility = View.GONE
            }
        }
    }


    override fun onDestroyView() {
        _binding = null // Evita fugas de memoria
        super.onDestroyView()
    }

    private fun initFactory() {
        val repository = HostRepository(requireContext())

        val viewModelFactory = AlmacenamientoViewModelFactory(repository, requireContext())
        viewModel =
            ViewModelProvider(
                this,
                viewModelFactory
            )[AlmacenamientoViewModel::class.java]
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AlmacenamientoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AlmacenamientoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
