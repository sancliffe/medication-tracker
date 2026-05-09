package android.template.ui

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * Represents a historical log of when a medication was taken.
 *
 * @property id The unique auto-generated identifier for this log entry.
 * @property medicationId The ID of the medication taken.
 * @property timestamp The exact time the medication was taken, in Unix milliseconds.
 */
@Entity(
    tableName = "dose_logs",
    foreignKeys = [
        ForeignKey(
            entity = Medication::class,
            parentColumns = ["id"],
            childColumns = ["medicationId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["medicationId"])]
)
data class DoseLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val medicationId: Long,
    val timestamp: Long
)
