package com.example.snmp.ui.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.snmp.ui.theme.SnmpTheme

class DescubrirHostDetalles : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            var shouldShowOnboarding by remember { mutableStateOf(true) }

            SnmpTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                ) { innerPadding ->
                    Greeting(
                        name = listOf("Android", "there"),
                        modifier = Modifier.padding(innerPadding),
                        shouldShowOnboarding = shouldShowOnboarding,
                        onContinueClicked = {
                            shouldShowOnboarding = false
                        } // Actualiza el estado correctamente
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(
    modifier: Modifier = Modifier,
    name: List<String>,
    shouldShowOnboarding: Boolean,
    onContinueClicked: () -> Unit // Callback para actualizar el estado
) {
    Surface(modifier = modifier) {
        if (shouldShowOnboarding) {
            OnboardingScreen(
                modifier = modifier,
                onContinueClicked = onContinueClicked
            )
        } else {
            GreetingVista(
                name = name,
                modifier = modifier
            )
        }
    }
}

@Composable
fun OnboardingScreen(modifier: Modifier = Modifier, onContinueClicked: () -> Unit) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome to the Basics Codelab!")
        Button(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = { onContinueClicked() } // Llama al callback para actualizar el estado
        ) {
            Text("Continue")
        }
    }
}

@Composable
fun GreetingVista(
    modifier: Modifier = Modifier,
    name: List<String>
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        for (n in name) {
            val expanded = remember { mutableStateOf(false) }
            val extraPadding = if (expanded.value) 25.dp else 0.dp
            Row(
                modifier = Modifier
                    .border(1.dp, Color.Black)

                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Hello $n! " + if (expanded.value) "Expanded" else "Not expanded",
                    modifier = modifier
                        .padding(bottom = extraPadding)
                        .weight(1f)
                )
                Button(
                    modifier = Modifier.padding(extraPadding),
                    onClick = {
                        expanded.value = !expanded.value
                    }
                ) {
                    Text("Click aquí")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SnmpTheme {
        Greeting(
            name = listOf("Android", "there"),
            shouldShowOnboarding = true,
            onContinueClicked = {} // Para el preview no se requiere funcionalidad
        )
    }
}
