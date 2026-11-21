
package com.example.tipcalc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.tipcalc.ui.theme.TipCalcTheme
import java.util.Locale
import kotlin.math.ceil

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TipCalcTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TipCalcScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun TipCalcScreen(modifier: Modifier = Modifier) {

    // Personalized values for matric number U23CP1002
    val defaultPeople = 4
    val defaultTip = 0.07f
    val roundingStep = 0.15

    var billText by rememberSaveable { mutableStateOf("") }
    var tipPercent by rememberSaveable { mutableFloatStateOf(defaultTip) }
    var people by rememberSaveable { mutableIntStateOf(defaultPeople) }

    val bill = billText.toDoubleOrNull()?.coerceAtLeast(0.0) ?: 0.0
    val tip = bill * tipPercent
    val total = bill + tip

    val rawPerPerson = total / people.coerceAtLeast(1)
    val perPerson = ceil(rawPerPerson / roundingStep - 0.000000001) * roundingStep

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Tip Calculator",
            style = MaterialTheme.typography.headlineMedium
        )

        Text("Calculate your tip and share the bill with friends.")

        OutlinedTextField(
            value = billText,
            onValueChange = { billText = it },
            label = { Text("Bill amount ($)") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Text("Tip: ${(tipPercent * 100).toInt()}%")

        Slider(
            value = tipPercent,
            onValueChange = { tipPercent = it },
            valueRange = 0f..0.30f,
            steps = 5
        )

        Text("Number of people: $people")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { if (people > 1) people-- },
                modifier = Modifier.weight(1f)
            ) {
                Text("−")
            }

            Button(
                onClick = { if (people < 50) people++ },
                modifier = Modifier.weight(1f)
            ) {
                Text("+")
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "Bill Summary",
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    "Tip amount: ${
                        String.format(Locale.US, "$%.2f", tip)
                    }"
                )

                Text(
                    "Total amount: ${
                        String.format(Locale.US, "$%.2f", total)
                    }",
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    "Per person: ${
                        String.format(Locale.US, "$%.2f", perPerson)
                    }",
                    style = MaterialTheme.typography.titleLarge
                )

                Text("Per-person amount is rounded up to the nearest $0.15.")
            }
        }

        Button(
            onClick = {
                billText = ""
                tipPercent = defaultTip
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Clear / Reset")
        }
    }
}
