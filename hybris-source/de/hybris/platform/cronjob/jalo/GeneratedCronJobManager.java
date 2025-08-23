package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.processengine.jalo.BusinessProcess;
import de.hybris.platform.processengine.jalo.BusinessProcessParameter;
import de.hybris.platform.processengine.jalo.DynamicProcessDefinition;
import de.hybris.platform.processengine.jalo.ProcessTask;
import de.hybris.platform.processengine.jalo.ProcessTaskLog;
import de.hybris.platform.processing.constants.GeneratedProcessingConstants;
import de.hybris.platform.processing.jalo.AfterRetentionCleanupRule;
import de.hybris.platform.processing.jalo.Batch;
import de.hybris.platform.processing.jalo.DistributedProcess;
import de.hybris.platform.processing.jalo.DistributedProcessTransitionTask;
import de.hybris.platform.processing.jalo.DistributedProcessWorkerTask;
import de.hybris.platform.processing.jalo.FlexibleSearchRetentionRule;
import de.hybris.platform.processing.jalo.SimpleBatch;
import de.hybris.platform.processing.jalo.SimpleDistributedProcess;
import de.hybris.platform.servicelayer.internal.jalo.DynamicMaintenanceCleanupJob;
import de.hybris.platform.servicelayer.internal.jalo.MaintenanceCleanupJob;
import de.hybris.platform.servicelayer.internal.jalo.RetentionJob;
import de.hybris.platform.servicelayer.internal.jalo.ScriptingJob;
import de.hybris.platform.servicelayer.internal.jalo.ServicelayerJob;
import de.hybris.platform.servicelayer.jalo.action.SimpleAction;
import de.hybris.platform.task.Task;
import de.hybris.platform.task.TaskCondition;
import de.hybris.platform.task.jalo.ScriptingTask;
import de.hybris.platform.task.jalo.TriggerTask;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCronJobManager extends Extension
{
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        DEFAULT_INITIAL_ATTRIBUTES = ttmp;
    }

    public Map<String, Item.AttributeMode> getDefaultAttributeModes(Class<? extends Item> itemClass)
    {
        Map<String, Item.AttributeMode> ret = new HashMap<>();
        Map<String, Item.AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
        if(attr != null)
        {
            ret.putAll(attr);
        }
        return ret;
    }


    public AfterRetentionCleanupRule createAfterRetentionCleanupRule(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.AFTERRETENTIONCLEANUPRULE);
            return (AfterRetentionCleanupRule)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating AfterRetentionCleanupRule : " + e.getMessage(), 0);
        }
    }


    public AfterRetentionCleanupRule createAfterRetentionCleanupRule(Map attributeValues)
    {
        return createAfterRetentionCleanupRule(getSession().getSessionContext(), attributeValues);
    }


    public Batch createBatch(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.BATCH);
            return (Batch)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Batch : " + e.getMessage(), 0);
        }
    }


    public Batch createBatch(Map attributeValues)
    {
        return createBatch(getSession().getSessionContext(), attributeValues);
    }


    public BatchJob createBatchJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.BATCHJOB);
            return (BatchJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating BatchJob : " + e.getMessage(), 0);
        }
    }


    public BatchJob createBatchJob(Map attributeValues)
    {
        return createBatchJob(getSession().getSessionContext(), attributeValues);
    }


    public BusinessProcess createBusinessProcess(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.BUSINESSPROCESS);
            return (BusinessProcess)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating BusinessProcess : " + e.getMessage(), 0);
        }
    }


    public BusinessProcess createBusinessProcess(Map attributeValues)
    {
        return createBusinessProcess(getSession().getSessionContext(), attributeValues);
    }


    public BusinessProcessParameter createBusinessProcessParameter(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.BUSINESSPROCESSPARAMETER);
            return (BusinessProcessParameter)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating BusinessProcessParameter : " + e.getMessage(), 0);
        }
    }


    public BusinessProcessParameter createBusinessProcessParameter(Map attributeValues)
    {
        return createBusinessProcessParameter(getSession().getSessionContext(), attributeValues);
    }


    public ChangeDescriptor createChangeDescriptor(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.CHANGEDESCRIPTOR);
            return (ChangeDescriptor)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ChangeDescriptor : " + e.getMessage(), 0);
        }
    }


    public ChangeDescriptor createChangeDescriptor(Map attributeValues)
    {
        return createChangeDescriptor(getSession().getSessionContext(), attributeValues);
    }


    public CleanUpCronJob createCleanUpCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.CLEANUPCRONJOB);
            return (CleanUpCronJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CleanUpCronJob : " + e.getMessage(), 0);
        }
    }


    public CleanUpCronJob createCleanUpCronJob(Map attributeValues)
    {
        return createCleanUpCronJob(getSession().getSessionContext(), attributeValues);
    }


    public CleanupDynamicProcessDefinitionsCronJob createCleanupDynamicProcessDefinitionsCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.CLEANUPDYNAMICPROCESSDEFINITIONSCRONJOB);
            return (CleanupDynamicProcessDefinitionsCronJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CleanupDynamicProcessDefinitionsCronJob : " + e.getMessage(), 0);
        }
    }


    public CleanupDynamicProcessDefinitionsCronJob createCleanupDynamicProcessDefinitionsCronJob(Map attributeValues)
    {
        return createCleanupDynamicProcessDefinitionsCronJob(getSession().getSessionContext(), attributeValues);
    }


    public CompositeCronJob createCompositeCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.COMPOSITECRONJOB);
            return (CompositeCronJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CompositeCronJob : " + e.getMessage(), 0);
        }
    }


    public CompositeCronJob createCompositeCronJob(Map attributeValues)
    {
        return createCompositeCronJob(getSession().getSessionContext(), attributeValues);
    }


    public CompositeEntry createCompositeEntry(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.COMPOSITEENTRY);
            return (CompositeEntry)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CompositeEntry : " + e.getMessage(), 0);
        }
    }


    public CompositeEntry createCompositeEntry(Map attributeValues)
    {
        return createCompositeEntry(getSession().getSessionContext(), attributeValues);
    }


    public CompositeJob createCompositeJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.COMPOSITEJOB);
            return (CompositeJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CompositeJob : " + e.getMessage(), 0);
        }
    }


    public CompositeJob createCompositeJob(Map attributeValues)
    {
        return createCompositeJob(getSession().getSessionContext(), attributeValues);
    }


    public CronJob createCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.CRONJOB);
            return (CronJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CronJob : " + e.getMessage(), 0);
        }
    }


    public CronJob createCronJob(Map attributeValues)
    {
        return createCronJob(getSession().getSessionContext(), attributeValues);
    }


    public CronJobHistory createCronJobHistory(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.CRONJOBHISTORY);
            return (CronJobHistory)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CronJobHistory : " + e.getMessage(), 0);
        }
    }


    public CronJobHistory createCronJobHistory(Map attributeValues)
    {
        return createCronJobHistory(getSession().getSessionContext(), attributeValues);
    }


    public CSVExportStep createCSVExportStep(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.CSVEXPORTSTEP);
            return (CSVExportStep)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CSVExportStep : " + e.getMessage(), 0);
        }
    }


    public CSVExportStep createCSVExportStep(Map attributeValues)
    {
        return createCSVExportStep(getSession().getSessionContext(), attributeValues);
    }


    public DistributedProcess createDistributedProcess(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.DISTRIBUTEDPROCESS);
            return (DistributedProcess)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating DistributedProcess : " + e.getMessage(), 0);
        }
    }


    public DistributedProcess createDistributedProcess(Map attributeValues)
    {
        return createDistributedProcess(getSession().getSessionContext(), attributeValues);
    }


    public DistributedProcessTransitionTask createDistributedProcessTransitionTask(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.DISTRIBUTEDPROCESSTRANSITIONTASK);
            return (DistributedProcessTransitionTask)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating DistributedProcessTransitionTask : " + e.getMessage(), 0);
        }
    }


    public DistributedProcessTransitionTask createDistributedProcessTransitionTask(Map attributeValues)
    {
        return createDistributedProcessTransitionTask(getSession().getSessionContext(), attributeValues);
    }


    public DistributedProcessWorkerTask createDistributedProcessWorkerTask(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.DISTRIBUTEDPROCESSWORKERTASK);
            return (DistributedProcessWorkerTask)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating DistributedProcessWorkerTask : " + e.getMessage(), 0);
        }
    }


    public DistributedProcessWorkerTask createDistributedProcessWorkerTask(Map attributeValues)
    {
        return createDistributedProcessWorkerTask(getSession().getSessionContext(), attributeValues);
    }


    public DynamicMaintenanceCleanupJob createDynamicMaintenanceCleanupJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.DYNAMICMAINTENANCECLEANUPJOB);
            return (DynamicMaintenanceCleanupJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating DynamicMaintenanceCleanupJob : " + e.getMessage(), 0);
        }
    }


    public DynamicMaintenanceCleanupJob createDynamicMaintenanceCleanupJob(Map attributeValues)
    {
        return createDynamicMaintenanceCleanupJob(getSession().getSessionContext(), attributeValues);
    }


    public DynamicProcessDefinition createDynamicProcessDefinition(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.DYNAMICPROCESSDEFINITION);
            return (DynamicProcessDefinition)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating DynamicProcessDefinition : " + e.getMessage(), 0);
        }
    }


    public DynamicProcessDefinition createDynamicProcessDefinition(Map attributeValues)
    {
        return createDynamicProcessDefinition(getSession().getSessionContext(), attributeValues);
    }


    public FlexibleSearchCronJob createFlexibleSearchCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.FLEXIBLESEARCHCRONJOB);
            return (FlexibleSearchCronJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating FlexibleSearchCronJob : " + e.getMessage(), 0);
        }
    }


    public FlexibleSearchCronJob createFlexibleSearchCronJob(Map attributeValues)
    {
        return createFlexibleSearchCronJob(getSession().getSessionContext(), attributeValues);
    }


    public FlexibleSearchRetentionRule createFlexibleSearchRetentionRule(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.FLEXIBLESEARCHRETENTIONRULE);
            return (FlexibleSearchRetentionRule)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating FlexibleSearchRetentionRule : " + e.getMessage(), 0);
        }
    }


    public FlexibleSearchRetentionRule createFlexibleSearchRetentionRule(Map attributeValues)
    {
        return createFlexibleSearchRetentionRule(getSession().getSessionContext(), attributeValues);
    }


    public GetURLStep createGetURLStep(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.GETURLSTEP);
            return (GetURLStep)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating GetURLStep : " + e.getMessage(), 0);
        }
    }


    public GetURLStep createGetURLStep(Map attributeValues)
    {
        return createGetURLStep(getSession().getSessionContext(), attributeValues);
    }


    public JobLog createJobLog(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.JOBLOG);
            return (JobLog)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating JobLog : " + e.getMessage(), 0);
        }
    }


    public JobLog createJobLog(Map attributeValues)
    {
        return createJobLog(getSession().getSessionContext(), attributeValues);
    }


    public JobMedia createJobMedia(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.JOBMEDIA);
            return (JobMedia)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating JobMedia : " + e.getMessage(), 0);
        }
    }


    public JobMedia createJobMedia(Map attributeValues)
    {
        return createJobMedia(getSession().getSessionContext(), attributeValues);
    }


    public JobSearchRestriction createJobSearchRestriction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.JOBSEARCHRESTRICTION);
            return (JobSearchRestriction)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating JobSearchRestriction : " + e.getMessage(), 0);
        }
    }


    public JobSearchRestriction createJobSearchRestriction(Map attributeValues)
    {
        return createJobSearchRestriction(getSession().getSessionContext(), attributeValues);
    }


    public LogFile createLogFile(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.LOGFILE);
            return (LogFile)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating LogFile : " + e.getMessage(), 0);
        }
    }


    public LogFile createLogFile(Map attributeValues)
    {
        return createLogFile(getSession().getSessionContext(), attributeValues);
    }


    public MaintenanceCleanupJob createMaintenanceCleanupJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.MAINTENANCECLEANUPJOB);
            return (MaintenanceCleanupJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating MaintenanceCleanupJob : " + e.getMessage(), 0);
        }
    }


    public MaintenanceCleanupJob createMaintenanceCleanupJob(Map attributeValues)
    {
        return createMaintenanceCleanupJob(getSession().getSessionContext(), attributeValues);
    }


    public MediaFolderStructureMigrationCronJob createMediaFolderStructureMigrationCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.MEDIAFOLDERSTRUCTUREMIGRATIONCRONJOB);
            return (MediaFolderStructureMigrationCronJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating MediaFolderStructureMigrationCronJob : " + e.getMessage(), 0);
        }
    }


    public MediaFolderStructureMigrationCronJob createMediaFolderStructureMigrationCronJob(Map attributeValues)
    {
        return createMediaFolderStructureMigrationCronJob(getSession().getSessionContext(), attributeValues);
    }


    public MediaProcessCronJob createMediaProcessCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.MEDIAPROCESSCRONJOB);
            return (MediaProcessCronJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating MediaProcessCronJob : " + e.getMessage(), 0);
        }
    }


    public MediaProcessCronJob createMediaProcessCronJob(Map attributeValues)
    {
        return createMediaProcessCronJob(getSession().getSessionContext(), attributeValues);
    }


    public MoveMediaCronJob createMoveMediaCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.MOVEMEDIACRONJOB);
            return (MoveMediaCronJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating MoveMediaCronJob : " + e.getMessage(), 0);
        }
    }


    public MoveMediaCronJob createMoveMediaCronJob(Map attributeValues)
    {
        return createMoveMediaCronJob(getSession().getSessionContext(), attributeValues);
    }


    public MoveMediaJob createMoveMediaJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.MOVEMEDIAJOB);
            return (MoveMediaJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating MoveMediaJob : " + e.getMessage(), 0);
        }
    }


    public MoveMediaJob createMoveMediaJob(Map attributeValues)
    {
        return createMoveMediaJob(getSession().getSessionContext(), attributeValues);
    }


    public ProcessTask createProcessTask(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.PROCESSTASK);
            return (ProcessTask)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ProcessTask : " + e.getMessage(), 0);
        }
    }


    public ProcessTask createProcessTask(Map attributeValues)
    {
        return createProcessTask(getSession().getSessionContext(), attributeValues);
    }


    public ProcessTaskLog createProcessTaskLog(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.PROCESSTASKLOG);
            return (ProcessTaskLog)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ProcessTaskLog : " + e.getMessage(), 0);
        }
    }


    public ProcessTaskLog createProcessTaskLog(Map attributeValues)
    {
        return createProcessTaskLog(getSession().getSessionContext(), attributeValues);
    }


    public ProcessTaskLogMaintenanceJob createProcessTaskLogMaintenanceJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.PROCESSTASKLOGMAINTENANCEJOB);
            return (ProcessTaskLogMaintenanceJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ProcessTaskLogMaintenanceJob : " + e.getMessage(), 0);
        }
    }


    public ProcessTaskLogMaintenanceJob createProcessTaskLogMaintenanceJob(Map attributeValues)
    {
        return createProcessTaskLogMaintenanceJob(getSession().getSessionContext(), attributeValues);
    }


    public RemoveItemsCronJob createRemoveItemsCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.REMOVEITEMSCRONJOB);
            return (RemoveItemsCronJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating RemoveItemsCronJob : " + e.getMessage(), 0);
        }
    }


    public RemoveItemsCronJob createRemoveItemsCronJob(Map attributeValues)
    {
        return createRemoveItemsCronJob(getSession().getSessionContext(), attributeValues);
    }


    public RemoveItemsJob createRemoveItemsJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.REMOVEITEMSJOB);
            return (RemoveItemsJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating RemoveItemsJob : " + e.getMessage(), 0);
        }
    }


    public RemoveItemsJob createRemoveItemsJob(Map attributeValues)
    {
        return createRemoveItemsJob(getSession().getSessionContext(), attributeValues);
    }


    public RetentionJob createRetentionJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.RETENTIONJOB);
            return (RetentionJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating RetentionJob : " + e.getMessage(), 0);
        }
    }


    public RetentionJob createRetentionJob(Map attributeValues)
    {
        return createRetentionJob(getSession().getSessionContext(), attributeValues);
    }


    public ScriptingJob createScriptingJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.SCRIPTINGJOB);
            return (ScriptingJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ScriptingJob : " + e.getMessage(), 0);
        }
    }


    public ScriptingJob createScriptingJob(Map attributeValues)
    {
        return createScriptingJob(getSession().getSessionContext(), attributeValues);
    }


    public ScriptingTask createScriptingTask(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.SCRIPTINGTASK);
            return (ScriptingTask)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ScriptingTask : " + e.getMessage(), 0);
        }
    }


    public ScriptingTask createScriptingTask(Map attributeValues)
    {
        return createScriptingTask(getSession().getSessionContext(), attributeValues);
    }


    public ServicelayerJob createServicelayerJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.SERVICELAYERJOB);
            return (ServicelayerJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ServicelayerJob : " + e.getMessage(), 0);
        }
    }


    public ServicelayerJob createServicelayerJob(Map attributeValues)
    {
        return createServicelayerJob(getSession().getSessionContext(), attributeValues);
    }


    public SimpleAction createSimpleAction(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.SIMPLEACTION);
            return (SimpleAction)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SimpleAction : " + e.getMessage(), 0);
        }
    }


    public SimpleAction createSimpleAction(Map attributeValues)
    {
        return createSimpleAction(getSession().getSessionContext(), attributeValues);
    }


    public SimpleBatch createSimpleBatch(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.SIMPLEBATCH);
            return (SimpleBatch)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SimpleBatch : " + e.getMessage(), 0);
        }
    }


    public SimpleBatch createSimpleBatch(Map attributeValues)
    {
        return createSimpleBatch(getSession().getSessionContext(), attributeValues);
    }


    public SimpleDistributedProcess createSimpleDistributedProcess(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.SIMPLEDISTRIBUTEDPROCESS);
            return (SimpleDistributedProcess)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SimpleDistributedProcess : " + e.getMessage(), 0);
        }
    }


    public SimpleDistributedProcess createSimpleDistributedProcess(Map attributeValues)
    {
        return createSimpleDistributedProcess(getSession().getSessionContext(), attributeValues);
    }


    public Task createTask(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.TASK);
            return (Task)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Task : " + e.getMessage(), 0);
        }
    }


    public Task createTask(Map attributeValues)
    {
        return createTask(getSession().getSessionContext(), attributeValues);
    }


    public TaskCondition createTaskCondition(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.TASKCONDITION);
            return (TaskCondition)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating TaskCondition : " + e.getMessage(), 0);
        }
    }


    public TaskCondition createTaskCondition(Map attributeValues)
    {
        return createTaskCondition(getSession().getSessionContext(), attributeValues);
    }


    public Trigger createTrigger(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.TRIGGER);
            return (Trigger)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Trigger : " + e.getMessage(), 0);
        }
    }


    public Trigger createTrigger(Map attributeValues)
    {
        return createTrigger(getSession().getSessionContext(), attributeValues);
    }


    public TriggerTask createTriggerTask(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.TRIGGERTASK);
            return (TriggerTask)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating TriggerTask : " + e.getMessage(), 0);
        }
    }


    public TriggerTask createTriggerTask(Map attributeValues)
    {
        return createTriggerTask(getSession().getSessionContext(), attributeValues);
    }


    public TypeSystemExportJob createTypeSystemExportJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.TYPESYSTEMEXPORTJOB);
            return (TypeSystemExportJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating TypeSystemExportJob : " + e.getMessage(), 0);
        }
    }


    public TypeSystemExportJob createTypeSystemExportJob(Map attributeValues)
    {
        return createTypeSystemExportJob(getSession().getSessionContext(), attributeValues);
    }


    public URLCronJob createURLCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedProcessingConstants.TC.URLCRONJOB);
            return (URLCronJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating URLCronJob : " + e.getMessage(), 0);
        }
    }


    public URLCronJob createURLCronJob(Map attributeValues)
    {
        return createURLCronJob(getSession().getSessionContext(), attributeValues);
    }


    public String getName()
    {
        return "processing";
    }
}
