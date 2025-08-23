package de.hybris.platform.ruleengineservices.jobs.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.enums.JobLogLevel;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.cronjob.jalo.CronJobProgressTracker;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.JobLogModel;
import de.hybris.platform.ruleengine.ResultItem;
import de.hybris.platform.ruleengine.RuleEngineActionResult;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerProblem;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerResult;
import de.hybris.platform.ruleengineservices.jobs.RuleEngineJobExecutionSynchronizer;
import de.hybris.platform.ruleengineservices.jobs.RuleEngineJobPerformable;
import de.hybris.platform.ruleengineservices.maintenance.RuleCompilerPublisherResult;
import de.hybris.platform.ruleengineservices.maintenance.RuleMaintenanceService;
import de.hybris.platform.ruleengineservices.model.RuleEngineCronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractRuleEngineJob extends AbstractJobPerformable<RuleEngineCronJobModel> implements RuleEngineJobPerformable<RuleEngineCronJobModel>
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractRuleEngineJob.class);
    protected static final PerformResult SUCCESS_RESULT = new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    protected static final PerformResult FAILURE_RESULT = new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
    protected static final PerformResult ABORTED_RESULT = new PerformResult(CronJobResult.ERROR, CronJobStatus.ABORTED);
    private RuleMaintenanceService ruleMaintenanceService;
    private RuleEngineJobExecutionSynchronizer ruleEngineJobExecutionSynchronizer;


    public PerformResult perform(RuleEngineCronJobModel cronJob)
    {
        Preconditions.checkArgument(Objects.nonNull(cronJob), "CronJob should be specified");
        CronJobProgressTracker tracker = createCronJobProgressTracker(cronJob);
        try
        {
            if(clearAbortRequestedIfNeeded((CronJobModel)cronJob))
            {
                return ABORTED_RESULT;
            }
            logOnJobStart();
            Optional<RuleCompilerPublisherResult> result = performInternal(cronJob, tracker);
            return result.isPresent() ? getPerformResult(cronJob, result.get()) : SUCCESS_RESULT;
        }
        catch(Exception e)
        {
            LOG.error("Exception caught: {}", e.getMessage());
            return FAILURE_RESULT;
        }
        finally
        {
            tracker.close();
        }
    }


    public boolean isAbortable()
    {
        return true;
    }


    public boolean isPerformable(RuleEngineCronJobModel cronJob)
    {
        return BooleanUtils.toBoolean(cronJob.getLockAcquired()) ? true : getRuleEngineJobExecutionSynchronizer().acquireLock(cronJob);
    }


    protected CronJobProgressTracker createCronJobProgressTracker(RuleEngineCronJobModel cronJob)
    {
        return new CronJobProgressTracker((CronJob)this.modelService.getSource(cronJob));
    }


    protected PerformResult getPerformResult(RuleEngineCronJobModel cronJob, RuleCompilerPublisherResult result)
    {
        if(hasErrors(result))
        {
            try
            {
                onError(cronJob, result);
            }
            catch(Exception e)
            {
                LOG.error("Error occurred: {}", e);
            }
            finally
            {
                logOnFailedJobFinish();
            }
            return FAILURE_RESULT;
        }
        if(result.getResult().equals(RuleCompilerPublisherResult.Result.SUCCESS))
        {
            logOnSuccessfulJobFinish();
            return SUCCESS_RESULT;
        }
        return FAILURE_RESULT;
    }


    protected boolean hasErrors(RuleCompilerPublisherResult result)
    {
        return (RuleCompilerPublisherResult.Result.COMPILER_ERROR.equals(result.getResult()) || RuleCompilerPublisherResult.Result.PUBLISHER_ERROR
                        .equals(result.getResult()) || hasPublisherErrors(result));
    }


    protected boolean hasPublisherErrors(RuleCompilerPublisherResult result)
    {
        return (Objects.nonNull(result.getPublisherResults()) && result
                        .getPublisherResults().stream().anyMatch(RuleEngineActionResult::isActionFailed));
    }


    protected void onError(RuleEngineCronJobModel cronJob, RuleCompilerPublisherResult ruleResults)
    {
        if(ruleResults.getResult() == RuleCompilerPublisherResult.Result.COMPILER_ERROR)
        {
            logCompilerErrorMessages(cronJob, ruleResults);
        }
        else if(ruleResults.getResult() == RuleCompilerPublisherResult.Result.PUBLISHER_ERROR)
        {
            logPublisherErrorMessages(cronJob, ruleResults);
        }
        else
        {
            throw new IllegalArgumentException("Unsupported error type " + ruleResults.getResult());
        }
    }


    protected void logCompilerErrorMessages(RuleEngineCronJobModel cronJob, RuleCompilerPublisherResult ruleResults)
    {
        Set<String> errorMessages = new HashSet<>();
        List<RuleCompilerResult> ruleCompilerResults = (List<RuleCompilerResult>)ruleResults.getCompilerResults().stream().filter(result -> RuleCompilerResult.Result.ERROR.equals(result.getResult())).collect(Collectors.toList());
        for(RuleCompilerResult ruleCompilerResult : ruleCompilerResults)
        {
            String ruleCode = ruleCompilerResult.getRuleCode();
            List<RuleCompilerProblem> problemsList = ruleCompilerResult.getProblems();
            for(RuleCompilerProblem ruleCompilerProblem : problemsList)
            {
                StringBuilder sb = new StringBuilder();
                sb.append("Rule Code ");
                sb.append("\"");
                sb.append(ruleCode);
                sb.append("\"");
                sb.append(": ");
                sb.append("\"");
                sb.append(ruleCompilerProblem.getMessage());
                sb.append("\"");
                errorMessages.add(sb.toString());
            }
        }
        Objects.requireNonNull(LOG);
        errorMessages.forEach(LOG::error);
        errorMessages.forEach(message -> logToDatabase(cronJob, message));
    }


    protected void logPublisherErrorMessages(RuleEngineCronJobModel cronJob, RuleCompilerPublisherResult ruleResults)
    {
        Set<String> errorMessages = (Set<String>)ruleResults.getPublisherResults().stream().flatMap(result -> result.getResults().stream()).map(ResultItem::getMessage).collect(Collectors.toSet());
        Objects.requireNonNull(LOG);
        errorMessages.forEach(LOG::error);
        errorMessages.forEach(message -> logToDatabase(cronJob, message));
    }


    protected void logToDatabase(RuleEngineCronJobModel cronJob, String error)
    {
        if(Boolean.TRUE.equals(cronJob.getLogToDatabase()))
        {
            JobLogModel log = (JobLogModel)getModelService().create(JobLogModel.class);
            log.setLevel(JobLogLevel.ERROR);
            log.setMessage(error);
            log.setCronJob((CronJobModel)cronJob);
            getModelService().save(log);
        }
    }


    protected void logOnJobStart()
    {
        LOG.info("*************************************");
        LOG.info("Starting {}", getJobName());
        LOG.info("*************************************");
    }


    protected void logOnSuccessfulJobFinish()
    {
        LOG.info("*************************************");
        LOG.info("{} successfully finished", getJobName());
        LOG.info("*************************************");
    }


    protected void logOnFailedJobFinish()
    {
        LOG.info("*************************************");
        LOG.info("{} finished with errors", getJobName());
        LOG.info("*************************************");
    }


    protected void setTrackerProgress(CronJobProgressTracker tracker, double progress)
    {
        tracker.setProgress(Double.valueOf(progress));
    }


    protected RuleMaintenanceService getRuleMaintenanceService()
    {
        return this.ruleMaintenanceService;
    }


    @Required
    public void setRuleMaintenanceService(RuleMaintenanceService ruleMaintenanceService)
    {
        this.ruleMaintenanceService = ruleMaintenanceService;
    }


    protected RuleEngineJobExecutionSynchronizer getRuleEngineJobExecutionSynchronizer()
    {
        return this.ruleEngineJobExecutionSynchronizer;
    }


    @Required
    public void setRuleEngineJobExecutionSynchronizer(RuleEngineJobExecutionSynchronizer ruleEngineJobExecutionSynchronizer)
    {
        this.ruleEngineJobExecutionSynchronizer = ruleEngineJobExecutionSynchronizer;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    protected abstract Optional<RuleCompilerPublisherResult> performInternal(RuleEngineCronJobModel paramRuleEngineCronJobModel, CronJobProgressTracker paramCronJobProgressTracker);


    protected abstract String getJobName();
}
