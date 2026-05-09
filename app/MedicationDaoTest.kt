package android.template.ui

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MedicationDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private lateinit var database: MedicationDatabase
    private lateinit var dao: MedicationDao

    @Before
    fun setup() {
        // Create an in-memory database so changes don't persist between test runs
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MedicationDatabase::class.java
        ).allowMainThreadQueries().build()
        
        dao = database.medicationDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndRetrieveMedication() {
        runBlocking {
            val medication = Medication(id = 1, name = "Vitamin C", dosage = "500mg", scheduledTime = "08:00")
            
            dao.insert(medication)

            // `.first()` collects the initial list emitted by Room and cancels the flow
            val medicationsList = dao.getAllMedications().first()

            assertEquals(1, medicationsList.size)
            assertEquals("Vitamin C", medicationsList[0].name)
        }
    }

    @Test
    fun insertLogAndRetrieveRelationalData() {
        runBlocking {
            val medication = Medication(id = 1, name = "Vitamin C", dosage = "500mg", scheduledTime = "08:00")
            dao.insert(medication)

            val log = DoseLog(id = 1, medicationId = 1, timestamp = 1680000000000L)
            dao.insertLog(log)

            val logsWithMeds = dao.getLogsWithMedications().first()

            assertEquals(1, logsWithMeds.size)
            assertEquals("Vitamin C", logsWithMeds[0].medication.name)
            assertEquals(1680000000000L, logsWithMeds[0].log.timestamp)
        }
    }
}