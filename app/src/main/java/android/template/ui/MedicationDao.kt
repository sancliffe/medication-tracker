package android.template.ui

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for interacting with the local SQLite database.
 * Handles all CRUD (Create, Read, Update, Delete) operations for [Medication] and [DoseLog] entities.
 */
@Dao
interface MedicationDao {
    /**
     * Retrieves a continuous stream of all medications.
     *
     * @return A [Flow] emitting the list of medications whenever the underlying data changes.
     */
    @Query("SELECT * FROM medications")
    fun getAllMedications(): Flow<List<Medication>>

    @Query("SELECT * FROM medications WHERE id = :id")
    suspend fun getMedicationById(id: Long): Medication?

    /**
     * Inserts a new medication into the database.
     * 
     * @return The row ID of the newly inserted medication.
     */
    @Insert
    suspend fun insert(medication: Medication): Long

    /**
     * Deletes a specific medication from the database.
     */
    @Delete
    suspend fun delete(medication: Medication)

    /**
     * Updates an existing medication in the database.
     */
    @Update
    suspend fun update(medication: Medication)

    /**
     * Inserts a new dose log into the database to record a taken medication.
     */
    @Insert
    suspend fun insertLog(log: DoseLog)

    /**
     * Retrieves a continuous stream of all dose logs, ordered by most recent first.
     *
     * @return A [Flow] emitting the history of taken medications.
     */
    @Transaction
    @Query("SELECT * FROM dose_logs ORDER BY timestamp DESC")
    fun getLogsWithMedications(): Flow<List<DoseLogWithMedication>>

    @Query("SELECT * FROM dose_logs ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<DoseLog>>
}
