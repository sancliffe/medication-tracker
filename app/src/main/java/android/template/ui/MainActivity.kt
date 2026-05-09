package android.template.ui

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

/**
 * The primary entry point for the application.
 * Displays the medication tracker UI using Jetpack Compose, including the list of medications
 * and the history of taken doses.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MedicationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
                var showAddDialog by remember { mutableStateOf(false) }
                var medicationToEdit by remember { mutableStateOf<Medication?>(null) }

                // Request POST_NOTIFICATIONS on Android 13+ 
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val permissionLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestPermission(),
                        onResult = { /* Handle permission grant/denial if necessary */ }
                    )
                    LaunchedEffect(Unit) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                }

            MaterialTheme {
                    Scaffold(
                        floatingActionButton = {
                            FloatingActionButton(onClick = { showAddDialog = true }) {
                                Icon(Icons.Default.Add, contentDescription = "Add Medication")
                            }
                        }
                    ) { paddingValues ->
                        Surface(
                            modifier = Modifier.fillMaxSize().padding(paddingValues),
                            color = MaterialTheme.colorScheme.background
                        ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Medication Tracker",
                            style = MaterialTheme.typography.headlineLarge
                        )

                        val medications = viewModel.medications.collectAsState(initial = emptyList())

                        Text(
                            text = "Your Medications",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(top = 16.dp)
                        )

                        if (medications.value.isEmpty()) {
                            Text(
                                text = "No medications yet",
                                modifier = Modifier.padding(8.dp)
                            )
                        } else {
                            LazyColumn {
                                items(medications.value) { medication ->
                                    Column(
                                        modifier = Modifier.padding(8.dp).fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = "${medication.name} - ${medication.dosage}",
                                                    style = MaterialTheme.typography.bodyLarge
                                                )
                                                Text(
                                                    text = "Time: ${medication.scheduledTime}",
                                                    style = MaterialTheme.typography.bodySmall
                                                )
                                            }
                                            Row {
                                                IconButton(onClick = { medicationToEdit = medication }) {
                                                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                                                }
                                                IconButton(onClick = { viewModel.deleteMedication(medication) }) {
                                                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                                                }
                                            }
                                        }
                                        Button(
                                            onClick = { viewModel.logDose(medication.id) },
                                            modifier = Modifier.padding(top = 4.dp)
                                        ) {
                                            Text("Log Dose")
                                        }
                                    }
                                }
                            }
                        }

                        val logs = viewModel.logsWithMedications.collectAsState(initial = emptyList())

                        Text(
                            text = "Dose History",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(top = 16.dp)
                        )

                        if (logs.value.isEmpty()) {
                            Text(
                                text = "No dose logs yet",
                                modifier = Modifier.padding(8.dp)
                            )
                        } else {
                            LazyColumn {
                                items(logs.value) { item ->
                                    @Suppress("NonObservableLocale")
                                    val formattedDate = java.text.SimpleDateFormat("MM/dd HH:mm", java.util.Locale.getDefault()).format(java.util.Date(item.log.timestamp))
                                    Text(
                                        text = "${item.medication.name} - $formattedDate",
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(4.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                if (showAddDialog || medicationToEdit != null) {
                    MedicationDialog(
                        initialMedication = medicationToEdit,
                        onDismiss = { 
                            showAddDialog = false
                            medicationToEdit = null
                        },
                        onSave = { name, dosage, time, notes, isRepeating ->
                            if (medicationToEdit != null) {
                                viewModel.updateMedication(medicationToEdit!!, name, dosage, time, notes, isRepeating)
                            } else {
                                viewModel.addMedication(name, dosage, time, notes, isRepeating)
                            }
                            showAddDialog = false
                            medicationToEdit = null
                        }
                    )
                }
                }
            }
        }
    }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationDialog(
    initialMedication: Medication? = null,
    onDismiss: () -> Unit,
    onSave: (name: String, dosage: String, time: String, notes: String, isRepeating: Boolean) -> Unit
) {
    var name by remember { mutableStateOf(initialMedication?.name ?: "") }
    var dosage by remember { mutableStateOf(initialMedication?.dosage ?: "") }
    var time by remember { mutableStateOf(initialMedication?.scheduledTime ?: "") }
    var notes by remember { mutableStateOf(initialMedication?.notes ?: "") }

    var showTimePicker by remember { mutableStateOf(false) }
    val initialHour = time.split(":").getOrNull(0)?.toIntOrNull() ?: 0
    val initialMinute = time.split(":").getOrNull(1)?.toIntOrNull() ?: 0
    val timePickerState = rememberTimePickerState(initialHour = initialHour, initialMinute = initialMinute)

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(if (initialMedication == null) "Add New Medication" else "Edit Medication", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name (e.g. Aspirin)") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = dosage,
                    onValueChange = { dosage = it },
                    label = { Text("Dosage (e.g. 1 pill)") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedButton(
                    onClick = { showTimePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (time.isEmpty()) "Select Time" else "Time: $time")
                }
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (optional)") }
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = { onSave(name, dosage, time, notes, true) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = name.isNotBlank() && dosage.isNotBlank() && time.isNotBlank()
                ) {
                    Text(if (initialMedication == null) "Save Medication" else "Update Medication")
                }
                TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                    Text("Cancel")
                }
            }
        }
    }

    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    time = String.format(Locale.getDefault(), "%02d:%02d", timePickerState.hour, timePickerState.minute)
                    showTimePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Cancel")
                }
            },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }
}
}
