package android.template.ui

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a medication to be tracked by the user.
 *
 * @property id The unique auto-generated identifier for this medication.
 * @property name The name of the medication.
 * @property dosage The dosage amount and unit (e.g., "200mg", "1 pill").
 * @property scheduledTime The time of day to take the medication, in "HH:mm" format.
 * @property isRepeating Whether the reminder should repeat daily.
 * @property notes Additional instructions for taking the medication.
 */
@Entity(tableName = "medications")
data class Medication(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val dosage: String,
    val scheduledTime: String,
    val isRepeating: Boolean = true,
    @ColumnInfo(defaultValue = "")
    val notes: String = ""
)
