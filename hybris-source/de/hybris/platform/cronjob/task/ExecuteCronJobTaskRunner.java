package de.hybris.platform.cronjob.task;

import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;
import javax.annotation.Resource;

public class ExecuteCronJobTaskRunner implements TaskRunner<TaskModel>
{
    public static String RUNNER_ID = "executeCronJobTaskRunner";
    private CronJobService cronJobService;


    public void run(TaskService taskService, TaskModel task) throws RetryLaterException
    {
        CronJobModel cronJob = (CronJobModel)task.getContextItem();
        this.cronJobService.performCronJob(cronJob, false);
    }


    public void handleError(TaskService taskService, TaskModel task, Throwable error)
    {
    }


    @Resource
    public void setCronJobService(CronJobService cronJobService)
    {
        this.cronJobService = cronJobService;
    }
}
