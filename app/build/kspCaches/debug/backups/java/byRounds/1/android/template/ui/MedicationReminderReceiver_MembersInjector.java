package android.template.ui;

import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class MedicationReminderReceiver_MembersInjector implements MembersInjector<MedicationReminderReceiver> {
  private final Provider<MedicationDao> medicationDaoProvider;

  private final Provider<AlarmScheduler> alarmSchedulerProvider;

  public MedicationReminderReceiver_MembersInjector(Provider<MedicationDao> medicationDaoProvider,
      Provider<AlarmScheduler> alarmSchedulerProvider) {
    this.medicationDaoProvider = medicationDaoProvider;
    this.alarmSchedulerProvider = alarmSchedulerProvider;
  }

  public static MembersInjector<MedicationReminderReceiver> create(
      Provider<MedicationDao> medicationDaoProvider,
      Provider<AlarmScheduler> alarmSchedulerProvider) {
    return new MedicationReminderReceiver_MembersInjector(medicationDaoProvider, alarmSchedulerProvider);
  }

  @Override
  public void injectMembers(MedicationReminderReceiver instance) {
    injectMedicationDao(instance, medicationDaoProvider.get());
    injectAlarmScheduler(instance, alarmSchedulerProvider.get());
  }

  @InjectedFieldSignature("android.template.ui.MedicationReminderReceiver.medicationDao")
  public static void injectMedicationDao(MedicationReminderReceiver instance,
      MedicationDao medicationDao) {
    instance.medicationDao = medicationDao;
  }

  @InjectedFieldSignature("android.template.ui.MedicationReminderReceiver.alarmScheduler")
  public static void injectAlarmScheduler(MedicationReminderReceiver instance,
      AlarmScheduler alarmScheduler) {
    instance.alarmScheduler = alarmScheduler;
  }
}
