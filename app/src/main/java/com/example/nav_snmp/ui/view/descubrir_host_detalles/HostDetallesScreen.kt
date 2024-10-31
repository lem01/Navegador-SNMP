package com.example.nav_snmp.ui.view.descubrir_host_detalles

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.example.nav_snmp.data.model.HostModelClass
import com.example.nav_snmp.ui.theme.SnmpTheme
import com.example.nav_snmp.ui.viewmodel.DescubrirHostViewModel
import id.ionbit.ionalert.IonAlert
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HostDetallesScreen(modifier: Modifier, descubrirHostDetalles: DescubrirHostDetalles) {
    val viewModel: DescubrirHostViewModel = DescubrirHostViewModel.descubrirHost

    LaunchedEffect(Unit) {
        viewModel.loadHosts() // Carga los hosts al inicio
    }

    Scaffold(modifier = modifier, topBar = {
        TopAppBar(

            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ), title = {
                Text(
                    "Descubrir Dispositivos", maxLines = 1, overflow = TextOverflow.Ellipsis
                )
            }, navigationIcon = {
                IconButton(onClick = {
                    descubrirHostDetalles.context.finish()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Localized description"
                    )
                }
            }, actions = {
                if (viewModel.isLoading.value) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                } else {

                    TextButton(onClick = {
                        viewModel.setHasLoadedHosts(false)

                        viewModel.viewModelScope.launch {
                            viewModel.loadHosts() // Carga los hosts
                        }
                    }) {
                        Text(text = "RECARGAR")
                    }
                }
            })
    },

        floatingActionButton = {
            BotonFlotante(descubrirHostDetalles)
        }) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

            if (DescubrirHostViewModel.descubrirHost.hostDescubiertos.isEmpty() && !viewModel.isLoading.value) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No se encontraron dispositivos")
                }
            } else if (viewModel.isLoading.value) {
                BarraDeProgreso()
            } else {
                ListaEncabezado()
                ListaHost(
                    modifier = Modifier.fillMaxSize(),
                    listaHost = viewModel.hostDescubiertos,
                    onCloseTask = { null },
                    onCheckedTask = { host, checked ->
                        viewModel.changeTaskChecked(host, checked)
                    },
                )
            }


        }
    }
}

@Composable
fun BotonFlotante(descubrirHostDetalles: DescubrirHostDetalles) {

    if (!DescubrirHostViewModel.descubrirHost.isLoading.value)
        ExtendedFloatingActionButton(
            elevation = FloatingActionButtonDefaults.elevation(),
            onClick = {
                if (!DescubrirHostViewModel.descubrirHost.hostDescubiertos.any { it.checked }) {
                    IonAlert(
                        descubrirHostDetalles.context, IonAlert.WARNING_TYPE
                    ).setTitleText("Advertencia")
                        .setContentText("Debe seleccionar al menos un dispositivo")
                        .show()
                    return@ExtendedFloatingActionButton
                }

                Toast.makeText(
                    descubrirHostDetalles.context,
                    "Guardando dispositivos seleccionados",
                    Toast.LENGTH_SHORT
                ).show()

            }) {
            Icon(imageVector = Icons.Default.AddCircle, contentDescription = "Guardar")
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Guardar")
        }
}

@Composable
fun ListaEncabezado() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            fontSize = 12.sp,
            text = "${DescubrirHostViewModel.descubrirHost.hostDescubiertos.size} dispositivos de " + "${DescubrirHostViewModel.descubrirHost._hostIntentados}",
            modifier = Modifier.padding(16.dp)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Seleccionar todos", fontSize = 12.sp)
            Checkbox(checked = DescubrirHostViewModel.descubrirHost.hostDescubiertos.all { it.checked },
                onCheckedChange = {
                    DescubrirHostViewModel.descubrirHost.seleccionarTodos(it)
                })
        }
    }

}

@Composable
fun BarraDeProgreso() {
    var progress by remember { mutableFloatStateOf(0f) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            loadProgress { progress = it }
        }
    }


    LinearProgressIndicator(
//        progress = { progress },
        color = MaterialTheme.colorScheme.secondary,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    )
}

suspend fun loadProgress(updateProgress: (Float) -> Unit) {
    for (i in 1..100) {
        updateProgress(i.toFloat() / 30)
        delay(1) // Simula un delay entre las actualizaciones
    }
}

@Composable
fun ListaHost(
    modifier: Modifier = Modifier,
    listaHost: List<HostModelClass>,
    onCheckedTask: (HostModelClass, Boolean) -> Unit,
    onCloseTask: (HostModelClass) -> Unit,
//    onCheckedTask: (Boolean) -> Unit,
) {

    LazyColumn(modifier = Modifier.padding(horizontal = 8.dp)) {
        items(items = listaHost, key = { host -> host.id }) { host ->
            DescubrirHostItem(
                hostModel = host,
                modifier = Modifier.fillMaxWidth(),
                checked = host.checked,
                onCheckedChange = { checked -> onCheckedTask(host, checked) },
                onClose = { onCloseTask(host) },

                )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable

fun BotonFlotantePreview() {

    val descubrirHostDetalles = DescubrirHostDetalles()

    //th
    SnmpTheme {
        Scaffold(floatingActionButton = { BotonFlotante(descubrirHostDetalles) }) {
            Box(modifier = Modifier.padding(it)) {}
        }
    }
}
