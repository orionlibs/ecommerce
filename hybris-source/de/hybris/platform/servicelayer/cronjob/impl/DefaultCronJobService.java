package de.hybris.platform.servicelayer.cronjob.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.cronjob.jalo.Job;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobLogModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.cronjob.task.ExecuteCronJobTaskRunner;
import de.hybris.platform.servicelayer.cluster.ClusterService;
import de.hybris.platform.servicelayer.cronjob.CronJobDao;
import de.hybris.platform.servicelayer.cronjob.CronJobFactory;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.cronjob.JobDao;
import de.hybris.platform.servicelayer.cronjob.JobLogDao;
import de.hybris.platform.servicelayer.cronjob.JobPerformable;
import de.hybris.platform.servicelayer.cronjob.RunCronJobMessageCreatorAndSender;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.internal.model.ServicelayerJobModel;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
import de.hybris.platform.servicelayer.tenant.TenantService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskService;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.GenericTypeResolver;

public class DefaultCronJobService extends AbstractBusinessService implements CronJobService
{
    protected static final int DEFAULT_LOG_BUNCH = 500;
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCronJobService.class);
    private static final int DEFAULT_EXECUTION_DELAY_IN_SECONDS = 30;
    private static final String CRONTAB_PATTERN = "{0} {1} {2} {3} {4} ? {5}";
    private CronJobDao cronJobDao;
    private JobDao jobDao;
    private JobLogDao jobLogDao;
    private ClusterService clusterService;
    private TaskService taskService;
    private Converter<List<JobLogModel>, String> jobLogConverter;
    private PersistentKeyGenerator cronJobCodeGenerator;


    public void performCronJob(CronJobModel cronJob, boolean synchronous)
    {
        CronJob cronJobItem = (CronJob)getModelService().getSource(cronJob);
        Job jobItem = cronJobItem.getJob();
        jobItem.perform(cronJobItem, synchronous);
    }


    public void performCronJob(CronJobModel cronJob)
    {
        Preconditions.checkArgument((cronJob != null), "CronJob model must not be null");
        Preconditions.checkArgument(!getModelService().isNew(cronJob), "CronJob model must be saved");
        if(canRunOnThisNode(cronJob))
        {
            performCronJob(cronJob, false);
        }
        else
        {
            Preconditions.checkState(this.clusterService.isClusteringEnabled(), "cannot start on other node since clustering is not enabled");
            startOnDifferentNodeViaTask(cronJob);
        }
    }


    protected boolean canRunOnThisNode(CronJobModel cronJob)
    {
        Integer fixedNode = cronJob.getNodeID();
        if(fixedNode != null)
        {
            return (fixedNode.intValue() == this.clusterService.getClusterId());
        }
        String clusterNodeGroup = cronJob.getNodeGroup();
        if(StringUtils.isNotBlank(clusterNodeGroup))
        {
            return this.clusterService.getClusterGroups().contains(clusterNodeGroup);
        }
        return true;
    }


    protected void startOnDifferentNodeViaTask(CronJobModel cronJob)
    {
        TaskModel executeTask = (TaskModel)getModelService().create(TaskModel.class);
        executeTask.setRunnerBean(ExecuteCronJobTaskRunner.RUNNER_ID);
        executeTask.setExecutionDate(new Date());
        executeTask.setContextItem((ItemModel)cronJob);
        executeTask.setNodeId(cronJob.getNodeID());
        executeTask.setNodeGroup(cronJob.getNodeGroup());
        this.taskService.scheduleTask(executeTask);
    }


    protected String getCronJobExpression(CronJobModel cronJob)
    {
        LocalDateTime time = getLocalDateTime();
        LOG.info("Setting up trigger for CronJob execution via TaskEngine. Execution will occur at {}. Configured nodeGroup: {}, nodeId: {}", new Object[] {time, cronJob
                        .getNodeGroup(), cronJob.getNodeID()});
        return MessageFormat.format("{0} {1} {2} {3} {4} ? {5}", new Object[] {String.valueOf(time.getSecond()),
                        String.valueOf(time.getMinute()),
                        String.valueOf(time.getHour()),
                        String.valueOf(time.getDayOfMonth()),
                        String.valueOf(time.getMonth().getValue()),
                        String.valueOf(time.getYear())});
    }


    LocalDateTime getLocalDateTime()
    {
        return LocalDateTime.now().plusSeconds(30L);
    }


    public void requestAbortCronJob(CronJobModel cronJob)
    {
        if(isRunning(cronJob))
        {
            if(!isAbortable(cronJob))
            {
                throw new IllegalStateException("job " + cronJob.getJob() + " is not abortable");
            }
            cronJob.setRequestAbort(Boolean.TRUE);
            getModelService().save(cronJob);
        }
    }


    public CronJobModel getCronJob(String code)
    {
        List<CronJobModel> result = this.cronJobDao.findCronJobs(code);
        ServicesUtil.validateIfSingleResult(result, "CronJob with code " + code + " not found", "More than one CronJob with code " + code + " found");
        return result.iterator().next();
    }


    public JobModel getJob(String code)
    {
        List<JobModel> result = this.jobDao.findJobs(code);
        ServicesUtil.validateIfSingleResult(result, "Job with code " + code + " not found", "More than one Job with code " + code + " found");
        return result.iterator().next();
    }


    public boolean isFinished(CronJobModel cronJob)
    {
        return (CronJobStatus.FINISHED.equals(cronJob.getStatus()) || CronJobStatus.ABORTED.equals(cronJob.getStatus()));
    }


    public boolean isPaused(CronJobModel cronJob)
    {
        return CronJobStatus.PAUSED.equals(cronJob.getStatus());
    }


    public boolean isRunning(CronJobModel cronJob)
    {
        return (CronJobStatus.RUNNING.equals(cronJob.getStatus()) || CronJobStatus.RUNNINGRESTART.equals(cronJob.getStatus()));
    }


    public boolean isSuccessful(CronJobModel cronJob)
    {
        if(!isFinished(cronJob))
        {
            throw new IllegalStateException("Cronjob " + cronJob.getCode() + " is still running. Can not check the result.");
        }
        return CronJobResult.SUCCESS.equals(cronJob.getResult());
    }


    public boolean isError(CronJobModel cronJob)
    {
        if(!isFinished(cronJob))
        {
            throw new IllegalStateException("Cronjob " + cronJob.getCode() + " is still running. Can not check the result.");
        }
        return (CronJobResult.ERROR.equals(cronJob.getResult()) || CronJobResult.FAILURE.equals(cronJob.getResult()));
    }


    public List<CronJobModel> getRunningOrRestartedCronJobs()
    {
        List<CronJobModel> result = this.cronJobDao.findRunningCronJobs();
        if(result == null)
        {
            throw new SystemException("Returned result for running cron jobs is null ");
        }
        return result;
    }


    public boolean isPerformable(CronJobModel cronJob)
    {
        return this.cronJobDao.isPerformable(cronJob);
    }


    public boolean isAbortable(CronJobModel cronJob)
    {
        return this.cronJobDao.isAbortable(cronJob);
    }


    public JobPerformable<? extends CronJobModel> getPerformable(ServicelayerJobModel slayerJobModel)
    {
        return (JobPerformable<? extends CronJobModel>)Registry.getCoreApplicationContext()
                        .getBean(slayerJobModel.getSpringId());
    }


    public <C extends CronJobModel, J extends JobModel> CronJobFactory<C, J> getCronJobFactory(ServicelayerJobModel slayerJobModel)
    {
        if(StringUtils.isNotBlank(slayerJobModel.getSpringIdCronJobFactory()))
        {
            return getExistingCronJobFactory(slayerJobModel);
        }
        return createGenericCronJobFactory(slayerJobModel);
    }


    private <C extends CronJobModel, J extends JobModel> CronJobFactory<C, J> getExistingCronJobFactory(ServicelayerJobModel slayerJobModel)
    {
        try
        {
            return (CronJobFactory<C, J>)Registry.getApplicationContext().getBean(slayerJobModel.getSpringIdCronJobFactory());
        }
        catch(BeansException exception)
        {
            throw new SystemException("Cannot return custom CronJobFactory for: '" + slayerJobModel.getClass().getSimpleName() + "' with code: '" + slayerJobModel
                            .getCode() + "' becasue provided CronJobFactory bean id doesn't exist within spring context.", exception);
        }
        catch(ClassCastException exception)
        {
            throw new SystemException("Cannot return custom CronJobFactory for: '" + slayerJobModel
                            .getClass().getSimpleName() + "' with code: '" + slayerJobModel
                            .getCode() + "' becasue provided CronJobFactory bean id indicates incorrect bean(that already exists within spring context).", exception);
        }
    }


    private <C extends CronJobModel, J extends JobModel> CronJobFactory<C, J> createGenericCronJobFactory(ServicelayerJobModel slayerJobModel)
    {
        Class<?> performableType = getPerformable(slayerJobModel).getClass();
        Class[] typeArgs = GenericTypeResolver.resolveTypeArguments(performableType, JobPerformable.class);
        Class<?> cronJobModelClass = (typeArgs == null) ? CronJobModel.class : typeArgs[0];
        return jobModel -> {
            try
            {
                CronJobModel cronJobModel = (CronJobModel)getModelService().create(cronJobModelClass);
                cronJobModel.setCode((String)this.cronJobCodeGenerator.generate());
                cronJobModel.setJob(jobModel);
                getModelService().save(cronJobModel);
                return cronJobModel;
            }
            catch(ModelSavingException e)
            {
                throw new SystemException("Error during saving the: '" + cronJobModelClass + "'. Please provide custom CronJobFactory via spring for ServicelayerJob: '" + jobModel.getCode() + "'.\nError due to '" + e.getMessage(), e);
            }
        };
    }


    public String getLogsAsText(CronJobModel cronJobModel)
    {
        List<JobLogModel> logs = this.jobLogDao.findJobLogs(cronJobModel, 500, true);
        return (String)this.jobLogConverter.convert(logs);
    }


    public String getLogsAsText(CronJobModel cronJobModel, int count)
    {
        if(count <= 0)
        {
            throw new SystemException("Provided count should be more than 0");
        }
        List<JobLogModel> logs = this.jobLogDao.findJobLogs(cronJobModel, count, true);
        return (String)this.jobLogConverter.convert(logs);
    }


    @Deprecated(since = "6.1.0", forRemoval = true)
    public void setRunCronJobMessageBuilder(RunCronJobMessageCreatorAndSender runCronJobMessageBuilder)
    {
        LOG.warn("RunCronJobMessageCreatorAndSender dependency has been removed from this class. This setter has been deprecated.");
    }


    @Deprecated(since = "6.1.0", forRemoval = true)
    public void setTenantService(TenantService tenantService)
    {
        LOG.warn("TenantService dependency has been removed from this class. This setter has been deprecated.");
    }


    @Required
    public void setClusterService(ClusterService clusterService)
    {
        this.clusterService = clusterService;
    }


    @Required
    public void setJobLogConverter(Converter<List<JobLogModel>, String> jobLogConverter)
    {
        this.jobLogConverter = jobLogConverter;
    }


    protected Converter<List<JobLogModel>, String> getJobLogConverter()
    {
        return this.jobLogConverter;
    }


    @Required
    public void setCronJobDao(CronJobDao cronJobDao)
    {
        this.cronJobDao = cronJobDao;
    }


    @Required
    public void setJobDao(JobDao jobDao)
    {
        this.jobDao = jobDao;
    }


    @Required
    public void setJobLogDao(JobLogDao jobLogDao)
    {
        this.jobLogDao = jobLogDao;
    }


    @Required
    public void setCronJobCodeGenerator(PersistentKeyGenerator cronJobCodeGenerator)
    {
        this.cronJobCodeGenerator = cronJobCodeGenerator;
    }


    @Required
    public void setTaskService(TaskService taskService)
    {
        this.taskService = taskService;
    }
}
