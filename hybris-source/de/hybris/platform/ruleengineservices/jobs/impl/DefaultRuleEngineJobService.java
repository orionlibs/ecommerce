package de.hybris.platform.ruleengineservices.jobs.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.ruleengineservices.jobs.RuleEngineCronJobDAO;
import de.hybris.platform.ruleengineservices.jobs.RuleEngineJobService;
import de.hybris.platform.ruleengineservices.model.RuleEngineCronJobModel;
import de.hybris.platform.ruleengineservices.model.RuleEngineJobModel;
import de.hybris.platform.servicelayer.cluster.ClusterService;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleEngineJobService implements RuleEngineJobService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultRuleEngineJobService.class);
    private ModelService modelService;
    private RuleEngineCronJobDAO ruleEngineCronJobDAO;
    private String triggerCronExpression;
    private CronJobService cronJobService;
    private ClusterService clusterService;


    public RuleEngineJobModel getRuleEngineJob(String jobCode, String springBeanName)
    {
        return getRuleEngineJob(jobCode)
                        .orElseGet(() -> createNewRuleEngineJob(jobCode, springBeanName));
    }


    public boolean isRunning(String ruleEngineJobCode)
    {
        return (countRunningJobs(ruleEngineJobCode) > 0);
    }


    public int countRunningJobs(String ruleEngineJobCode)
    {
        return getRuleEngineCronJobDAO().countCronJobsByJob(ruleEngineJobCode, new CronJobStatus[] {CronJobStatus.RUNNING, CronJobStatus.RUNNINGRESTART, CronJobStatus.UNKNOWN});
    }


    public RuleEngineCronJobModel triggerCronJob(String ruleEngineJobCode, String jobPerformableBeanName, Supplier<RuleEngineCronJobModel> cronJobSupplier)
    {
        Preconditions.checkArgument(Objects.nonNull(cronJobSupplier), "Cron Job supplier for RuleEngineCronJobModel should be provided");
        RuleEngineJobModel ruleEngineJob = getRuleEngineJob(ruleEngineJobCode, jobPerformableBeanName);
        RuleEngineCronJobModel ruleEngineCronJob = cronJobSupplier.get();
        LOG.debug("Triggering cron job [{}] for [{}]", ruleEngineCronJob.getCode(), ruleEngineJobCode);
        linkCronJobToJob(ruleEngineJob, ruleEngineCronJob);
        getModelService().save(ruleEngineCronJob);
        if(getCronJobService().isPerformable((CronJobModel)ruleEngineCronJob))
        {
            getCronJobService().performCronJob((CronJobModel)ruleEngineCronJob);
        }
        else
        {
            createTriggerForCronJob((CronJobModel)ruleEngineCronJob);
        }
        return ruleEngineCronJob;
    }


    protected void linkCronJobToJob(RuleEngineJobModel ruleEngineJob, RuleEngineCronJobModel ruleEngineCronJob)
    {
        ruleEngineCronJob.setJob((JobModel)ruleEngineJob);
        if(getClusterService().isClusteringEnabled())
        {
            if(ruleEngineJob.getNodeID() != null)
            {
                ruleEngineCronJob.setNodeID(ruleEngineJob.getNodeID());
            }
            if(StringUtils.isNotEmpty(ruleEngineJob.getNodeGroup()))
            {
                ruleEngineCronJob.setNodeGroup(ruleEngineJob.getNodeGroup());
            }
        }
    }


    protected Optional<RuleEngineJobModel> getRuleEngineJob(String jobCode)
    {
        return Optional.ofNullable(getRuleEngineCronJobDAO().findRuleEngineJobByCode(jobCode));
    }


    protected RuleEngineJobModel createNewRuleEngineJob(String jobCode, String springBeanName)
    {
        RuleEngineJobModel ruleEngineJob = (RuleEngineJobModel)getModelService().create(RuleEngineJobModel.class);
        ruleEngineJob.setCode(jobCode);
        ruleEngineJob.setSpringId(springBeanName);
        ruleEngineJob.setLogToDatabase(Boolean.valueOf(false));
        ruleEngineJob.setLogToFile(Boolean.valueOf(false));
        getModelService().save(ruleEngineJob);
        return ruleEngineJob;
    }


    protected void createTriggerForCronJob(CronJobModel cronJob)
    {
        LOG.debug("Creating trigger with cron expression [{}] for the cron job [{}]", getTriggerCronExpression(), cronJob
                        .getCode());
        TriggerModel trigger = (TriggerModel)getModelService().create(TriggerModel.class);
        trigger.setActivationTime(new Date());
        trigger.setCronJob(cronJob);
        trigger.setCronExpression(getTriggerCronExpression());
        getModelService().save(trigger);
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected RuleEngineCronJobDAO getRuleEngineCronJobDAO()
    {
        return this.ruleEngineCronJobDAO;
    }


    @Required
    public void setRuleEngineCronJobDAO(RuleEngineCronJobDAO ruleEngineCronJobDAO)
    {
        this.ruleEngineCronJobDAO = ruleEngineCronJobDAO;
    }


    public void setTriggerCronExpression(String triggerCronExpression)
    {
        this.triggerCronExpression = triggerCronExpression;
    }


    protected String getTriggerCronExpression()
    {
        return this.triggerCronExpression;
    }


    protected CronJobService getCronJobService()
    {
        return this.cronJobService;
    }


    @Required
    public void setCronJobService(CronJobService cronJobService)
    {
        this.cronJobService = cronJobService;
    }


    protected ClusterService getClusterService()
    {
        return this.clusterService;
    }


    @Required
    public void setClusterService(ClusterService clusterService)
    {
        this.clusterService = clusterService;
    }
}
