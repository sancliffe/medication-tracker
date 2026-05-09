package android.template.ui;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class MedicationViewModel_Factory implements Factory<MedicationViewModel> {
  private final Provider<MedicationDao> daoProvider;

  private final Provider<AlarmScheduler> alarmSchedulerProvider;

  public MedicationViewModel_Factory(Provider<MedicationDao> daoProvider,
      Provider<AlarmScheduler> alarmSchedulerProvider) {
    this.daoProvider = daoProvider;
    this.alarmSchedulerProvider = alarmSchedulerProvider;
  }

  @Override
  public MedicationViewModel get() {
    return newInstance(daoProvider.get(), alarmSchedulerProvider.get());
  }

  public static MedicationViewModel_Factory create(Provider<MedicationDao> daoProvider,
      Provider<AlarmScheduler> alarmSchedulerProvider) {
    return new MedicationViewModel_Factory(daoProvider, alarmSchedulerProvider);
  }

  public static MedicationViewModel newInstance(MedicationDao dao, AlarmScheduler alarmScheduler) {
    return new MedicationViewModel(dao, alarmScheduler);
  }
}
