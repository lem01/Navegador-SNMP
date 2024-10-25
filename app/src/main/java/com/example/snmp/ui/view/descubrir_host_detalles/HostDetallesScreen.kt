package com.example.snmp.ui.view.descubrir_host_detalles

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.snmp.data.model.HostModelClass
import com.example.snmp.ui.viewmodel.DescubrirHostViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HostDetallesScreen(modifier: Modifier, descubrirHostDetalles: DescubrirHostDetalles) {

    LaunchedEffect(Unit) {
        DescubrirHostViewModel.descubrirHost.loadHosts()
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        "Lista de Dispositivos",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        descubrirHostDetalles.context.finish();
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            if (DescubrirHostViewModel.descubrirHost.isLoading.value) {
                BarraDeProgreso()
            } else {
                ListaHost(
                    modifier = Modifier.fillMaxSize(),
                    listaHost = DescubrirHostViewModel.descubrirHost.hostDescubiertos,
                    onCloseTask = { null },
                    onCheckedTask = { host, checked ->
                        DescubrirHostViewModel.descubrirHost.changeTaskChecked(
                            host,
                            checked
                        )
                    },
                )
            }
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
        items(
            items = listaHost,
            key = { host -> host.id }
        ) { host ->
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

