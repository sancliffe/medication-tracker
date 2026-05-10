package android.template.ui

import androidx.collection.LongSparseArray
import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.appendPlaceholders
import androidx.room.util.getColumnIndex
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.room.util.recursiveFetchLongSparseArray
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.SQLiteStatement
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlin.text.StringBuilder
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class MedicationDao_Impl(
  __db: RoomDatabase,
) : MedicationDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfMedication: EntityInsertAdapter<Medication>

  private val __insertAdapterOfDoseLog: EntityInsertAdapter<DoseLog>

  private val __deleteAdapterOfMedication: EntityDeleteOrUpdateAdapter<Medication>

  private val __updateAdapterOfMedication: EntityDeleteOrUpdateAdapter<Medication>
  init {
    this.__db = __db
    this.__insertAdapterOfMedication = object : EntityInsertAdapter<Medication>() {
      protected override fun createQuery(): String = "INSERT OR ABORT INTO `medications` (`id`,`name`,`dosage`,`scheduledTime`,`isRepeating`,`notes`) VALUES (nullif(?, 0),?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Medication) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.dosage)
        statement.bindText(4, entity.scheduledTime)
        val _tmp: Int = if (entity.isRepeating) 1 else 0
        statement.bindLong(5, _tmp.toLong())
        statement.bindText(6, entity.notes)
      }
    }
    this.__insertAdapterOfDoseLog = object : EntityInsertAdapter<DoseLog>() {
      protected override fun createQuery(): String = "INSERT OR ABORT INTO `dose_logs` (`id`,`medicationId`,`timestamp`) VALUES (nullif(?, 0),?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: DoseLog) {
        statement.bindLong(1, entity.id)
        statement.bindLong(2, entity.medicationId)
        statement.bindLong(3, entity.timestamp)
      }
    }
    this.__deleteAdapterOfMedication = object : EntityDeleteOrUpdateAdapter<Medication>() {
      protected override fun createQuery(): String = "DELETE FROM `medications` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: Medication) {
        statement.bindLong(1, entity.id)
      }
    }
    this.__updateAdapterOfMedication = object : EntityDeleteOrUpdateAdapter<Medication>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `medications` SET `id` = ?,`name` = ?,`dosage` = ?,`scheduledTime` = ?,`isRepeating` = ?,`notes` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: Medication) {
        statement.bindLong(1, entity.id)
        statement.bindText(2, entity.name)
        statement.bindText(3, entity.dosage)
        statement.bindText(4, entity.scheduledTime)
        val _tmp: Int = if (entity.isRepeating) 1 else 0
        statement.bindLong(5, _tmp.toLong())
        statement.bindText(6, entity.notes)
        statement.bindLong(7, entity.id)
      }
    }
  }

  public override suspend fun insert(medication: Medication): Long = performSuspending(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfMedication.insertAndReturnId(_connection, medication)
    _result
  }

  public override suspend fun insertLog(log: DoseLog): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfDoseLog.insert(_connection, log)
  }

  public override suspend fun delete(medication: Medication): Unit = performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfMedication.handle(_connection, medication)
  }

  public override suspend fun update(medication: Medication): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfMedication.handle(_connection, medication)
  }

  public override fun getAllMedications(): Flow<List<Medication>> {
    val _sql: String = "SELECT * FROM medications"
    return createFlow(__db, false, arrayOf("medications")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfDosage: Int = getColumnIndexOrThrow(_stmt, "dosage")
        val _columnIndexOfScheduledTime: Int = getColumnIndexOrThrow(_stmt, "scheduledTime")
        val _columnIndexOfIsRepeating: Int = getColumnIndexOrThrow(_stmt, "isRepeating")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _result: MutableList<Medication> = mutableListOf()
        while (_stmt.step()) {
          val _item: Medication
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpDosage: String
          _tmpDosage = _stmt.getText(_columnIndexOfDosage)
          val _tmpScheduledTime: String
          _tmpScheduledTime = _stmt.getText(_columnIndexOfScheduledTime)
          val _tmpIsRepeating: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsRepeating).toInt()
          _tmpIsRepeating = _tmp != 0
          val _tmpNotes: String
          _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          _item = Medication(_tmpId,_tmpName,_tmpDosage,_tmpScheduledTime,_tmpIsRepeating,_tmpNotes)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getMedicationById(id: Long): Medication? {
    val _sql: String = "SELECT * FROM medications WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfName: Int = getColumnIndexOrThrow(_stmt, "name")
        val _columnIndexOfDosage: Int = getColumnIndexOrThrow(_stmt, "dosage")
        val _columnIndexOfScheduledTime: Int = getColumnIndexOrThrow(_stmt, "scheduledTime")
        val _columnIndexOfIsRepeating: Int = getColumnIndexOrThrow(_stmt, "isRepeating")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _result: Medication?
        if (_stmt.step()) {
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpDosage: String
          _tmpDosage = _stmt.getText(_columnIndexOfDosage)
          val _tmpScheduledTime: String
          _tmpScheduledTime = _stmt.getText(_columnIndexOfScheduledTime)
          val _tmpIsRepeating: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsRepeating).toInt()
          _tmpIsRepeating = _tmp != 0
          val _tmpNotes: String
          _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          _result = Medication(_tmpId,_tmpName,_tmpDosage,_tmpScheduledTime,_tmpIsRepeating,_tmpNotes)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getLogsWithMedications(): Flow<List<DoseLogWithMedication>> {
    val _sql: String = "SELECT * FROM dose_logs ORDER BY timestamp DESC"
    return createFlow(__db, true, arrayOf("medications", "dose_logs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfMedicationId: Int = getColumnIndexOrThrow(_stmt, "medicationId")
        val _columnIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _collectionMedication: LongSparseArray<Medication?> = LongSparseArray<Medication?>()
        while (_stmt.step()) {
          val _tmpKey: Long
          _tmpKey = _stmt.getLong(_columnIndexOfMedicationId)
          _collectionMedication.put(_tmpKey, null)
        }
        _stmt.reset()
        __fetchRelationshipmedicationsAsandroidTemplateUiMedication(_connection, _collectionMedication)
        val _result: MutableList<DoseLogWithMedication> = mutableListOf()
        while (_stmt.step()) {
          val _item: DoseLogWithMedication
          val _tmpLog: DoseLog
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpMedicationId: Long
          _tmpMedicationId = _stmt.getLong(_columnIndexOfMedicationId)
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_columnIndexOfTimestamp)
          _tmpLog = DoseLog(_tmpId,_tmpMedicationId,_tmpTimestamp)
          val _tmpMedication: Medication?
          val _tmpKey_1: Long
          _tmpKey_1 = _stmt.getLong(_columnIndexOfMedicationId)
          _tmpMedication = _collectionMedication.get(_tmpKey_1)
          if (_tmpMedication == null) {
            error("Relationship item 'medication' was expected to be NON-NULL but is NULL in @Relation involving a parent column named 'medicationId' and entityColumn named 'id'.")
          }
          _item = DoseLogWithMedication(_tmpLog,_tmpMedication)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getAllLogs(): Flow<List<DoseLog>> {
    val _sql: String = "SELECT * FROM dose_logs ORDER BY timestamp DESC"
    return createFlow(__db, false, arrayOf("dose_logs")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfMedicationId: Int = getColumnIndexOrThrow(_stmt, "medicationId")
        val _columnIndexOfTimestamp: Int = getColumnIndexOrThrow(_stmt, "timestamp")
        val _result: MutableList<DoseLog> = mutableListOf()
        while (_stmt.step()) {
          val _item: DoseLog
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpMedicationId: Long
          _tmpMedicationId = _stmt.getLong(_columnIndexOfMedicationId)
          val _tmpTimestamp: Long
          _tmpTimestamp = _stmt.getLong(_columnIndexOfTimestamp)
          _item = DoseLog(_tmpId,_tmpMedicationId,_tmpTimestamp)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  private fun __fetchRelationshipmedicationsAsandroidTemplateUiMedication(_connection: SQLiteConnection, _map: LongSparseArray<Medication?>) {
    if (_map.isEmpty()) {
      return
    }
    if (_map.size() > 999) {
      recursiveFetchLongSparseArray(_map, false) { _tmpMap ->
        __fetchRelationshipmedicationsAsandroidTemplateUiMedication(_connection, _tmpMap)
      }
      return
    }
    val _stringBuilder: StringBuilder = StringBuilder()
    _stringBuilder.append("SELECT `id`,`name`,`dosage`,`scheduledTime`,`isRepeating`,`notes` FROM `medications` WHERE `id` IN (")
    val _inputSize: Int = _map.size()
    appendPlaceholders(_stringBuilder, _inputSize)
    _stringBuilder.append(")")
    val _sql: String = _stringBuilder.toString()
    val _stmt: SQLiteStatement = _connection.prepare(_sql)
    var _argIndex: Int = 1
    for (i in 0 until _map.size()) {
      val _item: Long = _map.keyAt(i)
      _stmt.bindLong(_argIndex, _item)
      _argIndex++
    }
    try {
      val _itemKeyIndex: Int = getColumnIndex(_stmt, "id")
      if (_itemKeyIndex == -1) {
        return
      }
      val _columnIndexOfId: Int = 0
      val _columnIndexOfName: Int = 1
      val _columnIndexOfDosage: Int = 2
      val _columnIndexOfScheduledTime: Int = 3
      val _columnIndexOfIsRepeating: Int = 4
      val _columnIndexOfNotes: Int = 5
      while (_stmt.step()) {
        val _tmpKey: Long
        _tmpKey = _stmt.getLong(_itemKeyIndex)
        if (_map.containsKey(_tmpKey)) {
          val _item_1: Medication
          val _tmpId: Long
          _tmpId = _stmt.getLong(_columnIndexOfId)
          val _tmpName: String
          _tmpName = _stmt.getText(_columnIndexOfName)
          val _tmpDosage: String
          _tmpDosage = _stmt.getText(_columnIndexOfDosage)
          val _tmpScheduledTime: String
          _tmpScheduledTime = _stmt.getText(_columnIndexOfScheduledTime)
          val _tmpIsRepeating: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsRepeating).toInt()
          _tmpIsRepeating = _tmp != 0
          val _tmpNotes: String
          _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          _item_1 = Medication(_tmpId,_tmpName,_tmpDosage,_tmpScheduledTime,_tmpIsRepeating,_tmpNotes)
          _map.put(_tmpKey, _item_1)
        }
      }
    } finally {
      _stmt.close()
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
