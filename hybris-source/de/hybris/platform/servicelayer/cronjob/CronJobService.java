package de.hybris.platform.servicelayer.cronjob;

import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import java.util.List;

public interface CronJobService
{
    void performCronJob(CronJobModel paramCronJobModel, boolean paramBoolean);


    void performCronJob(CronJobModel paramCronJobModel);


    void requestAbortCronJob(CronJobModel paramCronJobModel);


    CronJobModel getCronJob(String paramString);


    JobModel getJob(String paramString);


    boolean isSuccessful(CronJobModel paramCronJobModel);


    boolean isError(CronJobModel paramCronJobModel);


    boolean isPaused(CronJobModel paramCronJobModel);


    boolean isRunning(CronJobModel paramCronJobModel);


    boolean isFinished(CronJobModel paramCronJobModel);


    List<CronJobModel> getRunningOrRestartedCronJobs();


    boolean isPerformable(CronJobModel paramCronJobModel);


    boolean isAbortable(CronJobModel paramCronJobModel);


    JobPerformable<? extends CronJobModel> getPerformable(ServicelayerJobModel paramServicelayerJobModel);


    <C extends CronJobModel, J extends JobModel> CronJobFactory<C, J> getCronJobFactory(ServicelayerJobModel paramServicelayerJobModel);


    String getLogsAsText(CronJobModel paramCronJobModel);


    String getLogsAsText(CronJobModel paramCronJobModel, int paramInt);
}
