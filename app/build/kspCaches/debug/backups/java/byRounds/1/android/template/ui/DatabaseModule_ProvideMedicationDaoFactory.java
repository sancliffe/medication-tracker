package android.template.ui;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class DatabaseModule_ProvideMedicationDaoFactory implements Factory<MedicationDao> {
  private final Provider<MedicationDatabase> databaseProvider;

  public DatabaseModule_ProvideMedicationDaoFactory(Provider<MedicationDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public MedicationDao get() {
    return provideMedicationDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideMedicationDaoFactory create(
      Provider<MedicationDatabase> databaseProvider) {
    return new DatabaseModule_ProvideMedicationDaoFactory(databaseProvider);
  }

  public static MedicationDao provideMedicationDao(MedicationDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideMedicationDao(database));
  }
}
