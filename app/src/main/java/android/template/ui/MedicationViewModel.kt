package android.template.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for managing the UI state of the medication tracker.
 * It interacts with [MedicationDao] to fetch data and log doses, exposing
 * [StateFlow]s that the Jetpack Compose UI can observe.
 *
 * @property dao The Data Access Object for database operations.
 */
@HiltViewModel
class MedicationViewModel @Inject constructor(
    private val dao: MedicationDao,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    /**
     * A [StateFlow] exposing the history of dose logs combined with their medication details.
     * Automatically updates whenever the database changes.
     */
    val logsWithMedications: StateFlow<List<DoseLogWithMedication>> = dao.getLogsWithMedications()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val medications: StateFlow<List<Medication>> = dao.getAllMedications()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /**
     * Adds a new medication to the database.
     */
    fun addMedication(name: String, dosage: String, scheduledTime: String, notes: String, isRepeating: Boolean) = viewModelScope.launch {
        val medication = Medication(
            name = name,
            dosage = dosage,
            scheduledTime = scheduledTime,
            isRepeating = isRepeating,
            notes = notes
        )
        
        // Retrieve the generated ID, apply it to our medication object, and schedule the alarm
        val insertedId = dao.insert(medication)
        val savedMedication = medication.copy(id = insertedId)
        alarmScheduler.scheduleMedicationAlarm(savedMedication)
    }

    /**
     * Logs a new dose for the given medication.
     *
     * @param medicationId The ID of the medication that was taken.
     */
    fun logDose(medicationId: Long) = viewModelScope.launch {
        dao.insertLog(DoseLog(medicationId = medicationId, timestamp = System.currentTimeMillis()))
    }

    /**
     * Deletes a medication from the database and cancels its associated alarm.
     *
     * @param medication The medication to delete.
     */
    fun deleteMedication(medication: Medication) = viewModelScope.launch {
        dao.delete(medication)
        alarmScheduler.cancelMedicationAlarm(medication)
    }

    /**
     * Updates an existing medication in the database and reschedules its alarm.
     */
    fun updateMedication(
        medication: Medication,
        name: String,
        dosage: String,
        scheduledTime: String,
        notes: String,
        isRepeating: Boolean
    ) = viewModelScope.launch {
        val updatedMedication = medication.copy(
            name = name,
            dosage = dosage,
            scheduledTime = scheduledTime,
            isRepeating = isRepeating,
            notes = notes
        )
        dao.update(updatedMedication)
        
        alarmScheduler.cancelMedicationAlarm(medication)
        alarmScheduler.scheduleMedicationAlarm(updatedMedication)
    }
}