package android.template.ui

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module that provides database-related dependencies.
 * This ensures that the Room database and DAOs are created only once (as Singletons)
 * and can be injected throughout the application.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // 1. Create a new table with the updated column name ("scheduledTime" instead of "time")
            db.execSQL("CREATE TABLE IF NOT EXISTS `medications_new` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `dosage` TEXT NOT NULL, `scheduledTime` TEXT NOT NULL, `isRepeating` INTEGER NOT NULL, `notes` TEXT NOT NULL DEFAULT '')")
            
            // 2. Copy the data over from the old table
            db.execSQL("INSERT INTO `medications_new` (`id`, `name`, `dosage`, `scheduledTime`, `isRepeating`, `notes`) SELECT `id`, `name`, `dosage`, `time`, `isRepeating`, `notes` FROM `medications`")
            
            // 3. Drop the old table
            db.execSQL("DROP TABLE `medications`")
            
            // 4. Rename the new table to the original name
            db.execSQL("ALTER TABLE `medications_new` RENAME TO `medications`")
        }
    }

    /**
     * Provides the singleton instance of [MedicationDatabase].
     * Configured to destructively migrate if the schema changes during development.
     */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MedicationDatabase {
        return Room.databaseBuilder(context, MedicationDatabase::class.java, "medication_db")
            .addMigrations(MIGRATION_2_3)
            .fallbackToDestructiveMigration(dropAllTables = false)
            .build()
    }

    /**
     * Provides the [MedicationDao] extracted from the database instance.
     * This is what gets injected into ViewModels and BroadcastReceivers.
     */
    @Provides
    fun provideMedicationDao(database: MedicationDatabase): MedicationDao = database.medicationDao()
}