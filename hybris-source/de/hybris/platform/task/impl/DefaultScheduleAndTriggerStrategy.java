package de.hybris.platform.task.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.internal.service.ServicelayerUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.task.Task;
import de.hybris.platform.task.TaskConditionModel;
import de.hybris.platform.task.TaskEvent;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.tx.DefaultTransaction;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.ReflectionUtils;

public class DefaultScheduleAndTriggerStrategy implements ScheduleAndTriggerStrategy
{
    private static final Logger LOG = Logger.getLogger(DefaultScheduleAndTriggerStrategy.class.getName());
    private ModelService modelService;
    private TaskDAO dao;


    public boolean triggerEvent(TaskEvent event)
    {
        Transaction tx = Transaction.current();
        boolean runningBefore = tx.isRunning();
        if(!runningBefore || !tx.isRollbackOnly())
        {
            Boolean doRepoll;
            tx.setTransactionIsolationLevel(2);
            try
            {
                doRepoll = (Boolean)tx.execute((TransactionBody)new Object(this, event));
            }
            catch(Exception e)
            {
                if(e instanceof RuntimeException)
                {
                    throw (RuntimeException)e;
                }
                throw new SystemException("Error triggering task event" + event + " : " + e.getMessage(), e);
            }
            return Boolean.TRUE.equals(doRepoll);
        }
        LOG.error("Not triggering event since current transaction is already marked as ROLLBACK ONLY.");
        return false;
    }


    protected boolean triggerEventInTransaction(TaskEvent event)
    {
        boolean repollApplicable = false;
        if(getDao().fulfillCondition(event))
        {
            repollApplicable = true;
        }
        else if(event.isFulfilNotExistingCondition())
        {
            repollApplicable = insertEventConditionOrRetryMatch(event);
            Transaction tx = Transaction.current();
            if(tx.isRollbackOnly())
            {
                ((DefaultTransaction)tx).clearRollbackOnly();
            }
        }
        return repollApplicable;
    }


    protected boolean insertEventConditionOrRetryMatch(TaskEvent event)
    {
        TaskConditionModel cond = (TaskConditionModel)this.modelService.create(TaskConditionModel.class);
        cond.setUniqueID(event.getId());
        cond.setFulfilled(Boolean.TRUE);
        cond.setExpirationDate(event.getExpirationDate());
        cond.setChoice(event.getChoice());
        try
        {
            this.modelService.save(cond);
            return true;
        }
        catch(ModelSavingException e)
        {
            if(isIgnorableScheduleConditionException(cond, (Exception)e))
            {
                return retryMatchConditionForEvent(event);
            }
            return false;
        }
    }


    protected boolean retryMatchConditionForEvent(TaskEvent event)
    {
        LOG.info("Retry match event condition [" + event + "].");
        return getDao().fulfillCondition(event);
    }


    protected boolean isIgnorableScheduleConditionException(TaskConditionModel condition, Exception e)
    {
        if(this.modelService.isUniqueConstraintErrorAsRootCause(e))
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Could not schedule task! The " + e.getClass().getSimpleName() + " has occurred with message: '" + e
                                .getMessage() + "'. Now attempting to run the transaction again!");
            }
            return true;
        }
        if(e instanceof RuntimeException)
        {
            throw (RuntimeException)e;
        }
        throw new SystemException("Could not schedule condition " + condition + " for task " + condition
                        .getTask() + " due to " + e
                        .getMessage(), e);
    }


    public boolean triggerEvent(String uniqueId)
    {
        return triggerEvent(TaskEvent.newEvent(uniqueId));
    }


    public boolean triggerEvent(String uniqueId, Date expirationDate)
    {
        TaskEvent event = TaskEvent.builder(uniqueId).withExpirationDate(expirationDate).build();
        return triggerEvent(event);
    }


    public void scheduleTask(TaskModel task)
    {
        scheduleTaskInternal(task);
    }


    protected void scheduleTaskInternal(TaskModel task)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug(String.format("Scheduling task %s for execution", new Object[] {task}));
        }
        try
        {
            Transaction tx = Transaction.current();
            tx.setTransactionIsolationLevel(2);
            tx.execute((TransactionBody)new Object(this, task));
        }
        catch(Exception e)
        {
            if(e instanceof RuntimeException)
            {
                throw (RuntimeException)e;
            }
            throw new SystemException("Error scheduling task " + task + " : " + e.getMessage(), e);
        }
    }


    protected void doScheduleInTransaction(TaskModel task)
    {
        if(CollectionUtils.isNotEmpty(task.getConditions()))
        {
            scheduleTaskWithConditions(task);
        }
        else
        {
            scheduleTaskWithoutConditions(task);
        }
    }


    protected void scheduleTaskWithoutConditions(TaskModel task)
    {
        this.modelService.save(task);
    }


    protected void scheduleTaskWithConditions(TaskModel task)
    {
        Date scheduledExecutionDateCopy = task.getExecutionDate();
        Set<TaskConditionModel> conditionsCopy = new LinkedHashSet<>(task.getConditions());
        prepareScheduleTaskWithConditions(task);
        Task taskItem = (Task)this.modelService.getSource(task);
        PK taskPK = taskItem.getPK();
        for(TaskConditionModel cond : conditionsCopy)
        {
            scheduleCondition(task, cond, taskPK);
        }
        finishScheduleTaskWithConditions(task, scheduledExecutionDateCopy);
    }


    private void scheduleCondition(TaskModel task, TaskConditionModel condition, PK taskPK)
    {
        if(!getDao().matchCondition(condition.getUniqueID(), taskPK))
        {
            insertScheduleConditionOrRetryMatch(task, taskPK, condition);
        }
    }


    protected void finishScheduleTaskWithConditions(TaskModel task, Date scheduledExecutionDate)
    {
        task.setExecutionDate((scheduledExecutionDate != null) ? scheduledExecutionDate : new Date());
        this.modelService.save(task);
        this.modelService.refresh(task);
    }


    protected void prepareScheduleTaskWithConditions(TaskModel task)
    {
        task.setExecutionDate(new Date(System.currentTimeMillis() + 86400000L));
        task.setConditions(Collections.EMPTY_SET);
        this.modelService.save(task);
    }


    protected void insertScheduleConditionOrRetryMatch(TaskModel task, PK taskPK, TaskConditionModel cond)
    {
        Transaction tx = Transaction.current();
        boolean txRollbackOnlyBefore = tx.isRollbackOnly();
        cond.setTask(task);
        try
        {
            this.modelService.save(cond);
        }
        catch(ModelSavingException e)
        {
            if(isIgnorableScheduleConditionException(cond, (Exception)e))
            {
                retryMatchConditionForSchedule(cond, taskPK, e);
                if(!txRollbackOnlyBefore && tx.isRollbackOnly())
                {
                    ((DefaultTransaction)tx).clearRollbackOnly();
                }
            }
        }
    }


    protected void retryMatchConditionForSchedule(TaskConditionModel cond, PK taskPK, ModelSavingException creationExcpetion)
    {
        LOG.info("Retrying to match condition [" + cond.getUniqueID() + "] for task with PK [" + taskPK + "]. Retrying to update already existing conditions.");
        if(!getDao().matchCondition(cond.getUniqueID(), taskPK))
        {
            LOG.error("Finally failed to match condition  [" + cond.getUniqueID() + "] for task " + taskPK + " during retry - aborting task scheduling now.");
            logProcessEngineDbConstraintViolations(cond);
            throw creationExcpetion;
        }
    }


    private void logProcessEngineDbConstraintViolations(TaskConditionModel unsavedTaskCondition)
    {
        LOG.error("Additional information about SAVED condition and UNSAVED condition[attempt to save causes SQLIntegrityConstraintViolationException] which have the same unique ID [" + unsavedTaskCondition
                        .getUniqueID() + "]");
        FlexibleSearchService flexibleSearchService = (FlexibleSearchService)ServicelayerUtils.getApplicationContext().getBean("flexibleSearchService");
        TaskConditionModel savedTaskCondition = (TaskConditionModel)this.modelService.create(TaskConditionModel.class);
        savedTaskCondition.setUniqueID(unsavedTaskCondition.getUniqueID());
        try
        {
            logProcessEngineDbConstraintViolationsInternal(unsavedTaskCondition, "UNSAVED condition");
            savedTaskCondition = (TaskConditionModel)flexibleSearchService.getModelByExample(savedTaskCondition);
            logProcessEngineDbConstraintViolationsInternal(savedTaskCondition, "SAVED condition");
        }
        catch(ModelNotFoundException ex)
        {
            LOG.error("[SAVED condition] The condition with unique ID [" + savedTaskCondition.getUniqueID() + "] should be found but wasn't found!");
        }
        catch(AmbiguousIdentifierException ex)
        {
            LOG.error("[SAVED condition] More than one condition with ID [" + savedTaskCondition.getUniqueID() + "] was found!");
        }
    }


    private void logProcessEngineDbConstraintViolationsInternal(TaskConditionModel taskCondition, String status)
    {
        TypeService typeService = (TypeService)ServicelayerUtils.getApplicationContext().getBean("typeService");
        ComposedTypeModel processTaskComposedType = typeService.getComposedTypeForCode("ProcessTask");
        if(typeService.isInstance((TypeModel)processTaskComposedType, taskCondition.getTask()))
        {
            try
            {
                Object businessProcessModel = ReflectionUtils.findMethod(taskCondition.getTask().getClass(), "getProcess").invoke(taskCondition.getTask(), new Object[0]);
                if(businessProcessModel == null)
                {
                    LOG.error("[" + status + "] The task which holds condition with unique ID [" + taskCondition.getUniqueID() + "] doesn't have set the business process.");
                    return;
                }
                String businessProcessCode = (String)ReflectionUtils.findMethod(businessProcessModel.getClass(), "getCode").invoke(businessProcessModel, new Object[0]);
                LOG.error("[" + status + "] The task which holds condition with unique ID [" + taskCondition.getUniqueID() + "] is in relation to the " + businessProcessCode + " process.");
            }
            catch(Exception ee)
            {
                LOG.error("[" + status + "] Error while calling method per reflection!", ee);
            }
        }
        else
        {
            LOG.error("[" + status + "] The condition with unique ID [" + taskCondition
                            .getUniqueID() + (
                            (taskCondition.getTask() != null) ? ("] doesn't belong to ProcessTask, but to: " +
                                            taskCondition.getTask().getClass().getSimpleName()) : "] does not have a assigned task") + ".");
        }
    }


    protected void disableNestedTransactions(SessionContext loclCtx)
    {
        loclCtx.setAttribute("transaction_in_create_disabled", Boolean.TRUE);
        loclCtx.setAttribute("enableTransactionalSaves", Boolean.FALSE);
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    private TaskDAO getDao()
    {
        return this.dao;
    }


    @Required
    public void setTaskDao(TaskDAO dao)
    {
        this.dao = dao;
    }
}
