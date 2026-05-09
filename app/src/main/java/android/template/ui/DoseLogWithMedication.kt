package android.template.ui

import androidx.room.Embedded
import androidx.room.Relation

/**
 * A relational data class that groups a [DoseLog] with its corresponding [Medication].
 * Room uses this to automatically fetch the linked medication when querying dose logs.
 */
data class DoseLogWithMedication(
    /**
     * The dose log entry containing the timestamp and medication ID.
     */
    @Embedded val log: DoseLog,
    
    /**
     * The medication details associated with the log entry.
     * Linked via the `medicationId` in [DoseLog] and `id` in [Medication].
     */
    @Relation(
        parentColumn = "medicationId",
        entityColumn = "id"
    )
    val medication: Medication
)