package android.template.ui

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class MedicationDatabase_Impl : MedicationDatabase() {
  private val _medicationDao: Lazy<MedicationDao> = lazy {
    MedicationDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(3, "65c97eb9fe223082cac08acc8e9c28a1", "f81867dbb1c60a3cfe65133139ee3ac1") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `medications` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `dosage` TEXT NOT NULL, `scheduledTime` TEXT NOT NULL, `isRepeating` INTEGER NOT NULL, `notes` TEXT NOT NULL DEFAULT '')")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `dose_logs` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `medicationId` INTEGER NOT NULL, `timestamp` INTEGER NOT NULL, FOREIGN KEY(`medicationId`) REFERENCES `medications`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
        connection.execSQL("CREATE INDEX IF NOT EXISTS `index_dose_logs_medicationId` ON `dose_logs` (`medicationId`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '65c97eb9fe223082cac08acc8e9c28a1')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `medications`")
        connection.execSQL("DROP TABLE IF EXISTS `dose_logs`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        connection.execSQL("PRAGMA foreign_keys = ON")
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection): RoomOpenDelegate.ValidationResult {
        val _columnsMedications: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsMedications.put("id", TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMedications.put("name", TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMedications.put("dosage", TableInfo.Column("dosage", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMedications.put("scheduledTime", TableInfo.Column("scheduledTime", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMedications.put("isRepeating", TableInfo.Column("isRepeating", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsMedications.put("notes", TableInfo.Column("notes", "TEXT", true, 0, "''", TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysMedications: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesMedications: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoMedications: TableInfo = TableInfo("medications", _columnsMedications, _foreignKeysMedications, _indicesMedications)
        val _existingMedications: TableInfo = read(connection, "medications")
        if (!_infoMedications.equals(_existingMedications)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |medications(android.template.ui.Medication).
              | Expected:
              |""".trimMargin() + _infoMedications + """
              |
              | Found:
              |""".trimMargin() + _existingMedications)
        }
        val _columnsDoseLogs: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsDoseLogs.put("id", TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDoseLogs.put("medicationId", TableInfo.Column("medicationId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsDoseLogs.put("timestamp", TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysDoseLogs: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        _foreignKeysDoseLogs.add(TableInfo.ForeignKey("medications", "CASCADE", "NO ACTION", listOf("medicationId"), listOf("id")))
        val _indicesDoseLogs: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesDoseLogs.add(TableInfo.Index("index_dose_logs_medicationId", false, listOf("medicationId"), listOf("ASC")))
        val _infoDoseLogs: TableInfo = TableInfo("dose_logs", _columnsDoseLogs, _foreignKeysDoseLogs, _indicesDoseLogs)
        val _existingDoseLogs: TableInfo = read(connection, "dose_logs")
        if (!_infoDoseLogs.equals(_existingDoseLogs)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |dose_logs(android.template.ui.DoseLog).
              | Expected:
              |""".trimMargin() + _infoDoseLogs + """
              |
              | Found:
              |""".trimMargin() + _existingDoseLogs)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "medications", "dose_logs")
  }

  public override fun clearAllTables() {
    super.performClear(true, "medications", "dose_logs")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(MedicationDao::class, MedicationDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>): List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun medicationDao(): MedicationDao = _medicationDao.value
}
