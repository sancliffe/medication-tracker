package android.template.ui

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * The primary Room database class for the Medication Tracker application.
 * Defines the entities used in the database and provides access to the DAOs.
 */
@Database(
    entities = [Medication::class, DoseLog::class],
    version = 3,
    exportSchema = false
)
abstract class MedicationDatabase : RoomDatabase() {
    
    /**
     * Provides the Data Access Object (DAO) for medications and dose logs.
     */
    abstract fun medicationDao(): MedicationDao
    
}