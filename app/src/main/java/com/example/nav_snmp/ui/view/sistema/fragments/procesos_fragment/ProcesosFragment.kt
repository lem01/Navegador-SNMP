package com.example.nav_snmp.ui.view.sistema.fragments.procesos_fragment

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ScrollView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.nav_snmp.R
import com.example.nav_snmp.data.repository.HostRepository
import com.example.nav_snmp.databinding.FragmentProcesosBinding
import com.example.nav_snmp.ui.view.sistema.fragments.almacenamiento_fragment.AlmacenamientoViewModel
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [ProcesosFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProcesosFragment : Fragment() {
    private lateinit var viewModel: ProcesosViewModel
    private var _binding: FragmentProcesosBinding? = null
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
        initTableLayout(viewModel)

    }

    private fun initTableLayout(viewModel: ProcesosViewModel) {
        val headerRow = TableRow(requireContext()).apply {
            showDividers = TableRow.SHOW_DIVIDER_MIDDLE and TableRow.SHOW_DIVIDER_BEGINNING and TableRow.SHOW_DIVIDER_END
            dividerDrawable =
                resources.getDrawable(android.R.drawable.divider_horizontal_dark, null)

            val headers = listOf("Indice", "Descripción", "Estado", "Errores")
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

        val scrollView = ScrollView(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

        val contentTableLayout = TableLayout(requireContext()).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        viewModel.procesoModel.observe(viewLifecycleOwner) {
//            contentTableLayout.removeAllViews()

            it.map { proceso ->
                val tableRow = TableRow(requireContext()).apply {
                    layoutParams = TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                    )
                }

                // Crear y agregar cada TextView con un divisor
                val textView1 = TextView(requireContext()).apply {
                    text = proceso.indice
                    layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                    gravity = Gravity.CENTER
                    setPadding(5.dp.value.toInt(), 5.dp.value.toInt(), 5.dp.value.toInt(), 8.dp.value.toInt())
                }

                val textView2 = TextView(requireContext()).apply {
                    text = proceso.descripcion
                    layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                    gravity = Gravity.CENTER
                    setPadding(5.dp.value.toInt(), 5.dp.value.toInt(), 5.dp.value.toInt(), 8.dp.value.toInt())
                }

                val textView3 = TextView(requireContext()).apply {
                    text = proceso.estado
                    layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                    gravity = Gravity.CENTER
                    setPadding(5.dp.value.toInt(), 5.dp.value.toInt(), 5.dp.value.toInt(), 8.dp.value.toInt())
                }

                val textView4 = TextView(requireContext()).apply {
                    text = proceso.errores
                    layoutParams = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f)
                    gravity = Gravity.CENTER
                    setPadding(5.dp.value.toInt(), 5.dp.value.toInt(), 5.dp.value.toInt(), 8.dp.value.toInt())
                }

                // Agregar los TextViews y los divisores verticales
                tableRow.addView(textView1)
                tableRow.addView(createDivider()) // Divisor vertical
                tableRow.addView(textView2)
                tableRow.addView(createDivider()) // Divisor vertical
                tableRow.addView(textView3)
                tableRow.addView(createDivider()) // Divisor vertical
                tableRow.addView(textView4)

                // Envolver el TableRow en un FrameLayout para divisores horizontales
                val frameLayout = FrameLayout(requireContext()).apply {
                    background = ContextCompat.getDrawable(requireContext(), R.drawable.table_row_divider)
                    addView(tableRow)
                }

                // Agregar el FrameLayout al TableLayout
                contentTableLayout.addView(frameLayout)
            }


        }

        // Agregar el TableLayout de contenido al ScrollView
        scrollView.addView(contentTableLayout)

        // Finalmente, agregar el ScrollView al TableLayout principal
        binding.tableLayout.addView(scrollView)
    }

    private fun createDivider(): View {
        return View(requireContext()).apply {
            layoutParams = TableRow.LayoutParams(1.dp.value.toInt(), TableRow.LayoutParams.MATCH_PARENT)
            setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.darker_gray))
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
        //todo
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
        // TODO: Rename and change types and number of parameters
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
