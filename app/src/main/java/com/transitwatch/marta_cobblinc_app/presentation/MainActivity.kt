package com.example.transitwatch

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                TransitScreen()
            }
        }
    }
}

@Composable
fun TransitScreen(vm: TransitViewModel = viewModel()) {
    val state by vm.state.collectAsState()

    Scaffold(
        timeText = { TimeText() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            when {
                state.loading -> {
                    Text(text = "Loading...")
                }
                state.error != null -> {
                    Text(text = "Error: ${state.error}")
                }
                else -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = state.stopName ?: "",
                            style = MaterialTheme.typography.caption1
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = state.routeName ?: "",
                            style = MaterialTheme.typography.caption2
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        val mins = (state.effectiveLeftSeconds ?: 0) / 60
                        Text(
                            text = "$mins min",
                            style = MaterialTheme.typography.display1,
                            color = urgencyColor(state.urgency)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun urgencyColor(urgency: String?): Color =
    when (urgency) {
        "RED" -> Color(0xFFFF5252)
        "YELLOW" -> Color(0xFFFFC107)
        else -> Color(0xFF69F0AE)
    }
