package de.hybris.platform.processengine.impl;

import de.hybris.platform.processengine.BusinessProcessEvent;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.definition.ContextParameterDeclaration;
import de.hybris.platform.processengine.definition.Node;
import de.hybris.platform.processengine.definition.ProcessDefinition;
import de.hybris.platform.processengine.definition.ProcessDefinitionFactory;
import de.hybris.platform.processengine.definition.ProcessDefinitionId;
import de.hybris.platform.processengine.definition.UnsatisfiedContextParameterException;
import de.hybris.platform.processengine.enums.ProcessState;
import de.hybris.platform.processengine.helpers.ProcessFactory;
import de.hybris.platform.processengine.helpers.ProcessParameterHelper;
import de.hybris.platform.processengine.jalo.BusinessProcess;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.model.ProcessTaskModel;
import de.hybris.platform.processengine.task.BusinessProcessRestartStrategy;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.task.TaskEvent;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskService;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import de.hybris.platform.util.Config;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

public class DefaultBusinessProcessService extends AbstractBusinessService implements BusinessProcessService
{
    public static final String PROCESSENGINE_PROCESS_RESTART_LEGACY = "processengine.process.restart.legacy";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultBusinessProcessService.class);
    private ProcessDefinitionFactory processDefinitionFactory;
    private ProcessFactory processFactory;
    private ProcessParameterHelper processParameterHelper;
    private TaskService taskService;
    private BusinessProcessServiceDao businessProcessServiceDao;
    private ConfigurationService configurationService;
    private TransactionTemplate transactionTemplate;
    private BusinessProcessRestartStrategy businessProcessRestartStrategy;


    public <T extends BusinessProcessModel> T createProcess(String processCode, String processDefinitionName)
    {
        return createProcess(processCode, processDefinitionName, null);
    }


    public <T extends BusinessProcessModel> T createProcess(String processCode, String processDefinitionName, Map<String, Object> contextParameters)
    {
        BusinessProcessModel businessProcessModel = this.processFactory.createProcessModel(processCode, processDefinitionName, contextParameters);
        logDebug("Setting state of process with code {} to CREATED", new Object[] {processCode});
        businessProcessModel.setState(ProcessState.CREATED);
        getModelService().save(businessProcessModel);
        return (T)businessProcessModel;
    }


    public <T extends BusinessProcessModel> T startProcess(String processCode, String processDefinitionName, Map<String, Object> contextParameters)
    {
        return (T)this.transactionTemplate.execute((TransactionCallback)new Object(this, processCode, processDefinitionName, contextParameters));
    }


    public <T extends BusinessProcessModel> T startProcess(String processCode, String processDefinitionName)
    {
        return startProcess(processCode, processDefinitionName, null);
    }


    public void startProcess(BusinessProcessModel process)
    {
        if(process == null)
        {
            throw new IllegalArgumentException("Business process must not be null");
        }
        logDebug("Starting process with code {}", new Object[] {process.getCode()});
        ProcessDefinition processDefinition = this.processDefinitionFactory.getProcessDefinition(
                        ProcessDefinitionId.of(process));
        validateContext(process, processDefinition);
        this.transactionTemplate.execute((TransactionCallback)new Object(this, processDefinition, process));
    }


    protected void doStartProcessInsideTx(ProcessDefinition processDefinition, BusinessProcessModel process)
    {
        processDefinition.start(process);
        if(LOG.isDebugEnabled())
        {
            logDebug("Setting state of process with code {} to RUNNING", new Object[] {process.getCode()});
        }
        process.setState(ProcessState.RUNNING);
        getModelService().save(process);
    }


    public void restartProcess(BusinessProcessModel process, String nodeId)
    {
        logDebug("Restarting process: {} on node {}", new Object[] {process, nodeId});
        this.transactionTemplate.execute((TransactionCallback)new Object(this, process, nodeId));
    }


    private boolean isProcessWaitingOnAllActions(BusinessProcessModel process)
    {
        ProcessDefinitionId procDefId = new ProcessDefinitionId(process.getProcessDefinitionName());
        ProcessDefinition processDefinition = this.processDefinitionFactory.getProcessDefinition(procDefId);
        getModelService().refresh(process);
        Collection<ProcessTaskModel> processCurrentTasks = process.getCurrentTasks();
        for(ProcessTaskModel pt : processCurrentTasks)
        {
            String actionName = pt.getAction();
            Node node = processDefinition.retrieve(actionName);
            if(!Node.isWaitNode(node))
            {
                return false;
            }
        }
        return true;
    }


    private void deleteCurrentTasksAndAssociatedConditions(BusinessProcessModel process)
    {
        for(ProcessTaskModel pt : process.getCurrentTasks())
        {
            getModelService().removeAll(pt.getConditions());
        }
        getModelService().removeAll(process.getCurrentTasks());
    }


    protected void doRestartProcessInsideTx(BusinessProcessModel process, String nodeId)
    {
        if(!Config.getBoolean("processengine.process.restart.legacy", false) && !makeARequestToRestartProcess(process))
        {
            return;
        }
        if(isProcessWaitingOnAllActions(process))
        {
            deleteCurrentTasksAndAssociatedConditions(process);
        }
        ProcessTaskModel task = new ProcessTaskModel();
        task.setAction(nodeId);
        task.setExecutionDate(new Date());
        task.setProcess(process);
        task.setRunnerBean("taskRunner");
        logDebug("Scheduling action with ID {} for process with code {}", new Object[] {nodeId, process.getCode()});
        this.taskService.scheduleTask((TaskModel)task);
        process.setState(ProcessState.RUNNING);
        getModelService().save(process);
    }


    private boolean makeARequestToRestartProcess(BusinessProcessModel process)
    {
        return (this.businessProcessRestartStrategy == null || this.businessProcessRestartStrategy.makeARequestToRestartProcess(process));
    }


    public <T extends BusinessProcessModel> T getProcess(String processName)
    {
        return (T)this.businessProcessServiceDao.findProcessByName(processName);
    }


    protected void validateContext(BusinessProcessModel processModel, ProcessDefinition processDefinition)
    {
        for(String contextParameterName : processDefinition.allContextParameterNames())
        {
            ContextParameterDeclaration parameterDeclaration = processDefinition.getContextParameterDeclaration(contextParameterName);
            if(parameterDeclaration.isRequired())
            {
                if(this.processParameterHelper.getProcessParameterByName(parameterDeclaration.getName(), processModel
                                .getContextParameters()) == null)
                {
                    throw new UnsatisfiedContextParameterException(parameterDeclaration);
                }
            }
        }
    }


    public ProcessDefinitionFactory getProcessDefinitionFactory()
    {
        return this.processDefinitionFactory;
    }


    @Required
    public void setProcessDefinitionFactory(ProcessDefinitionFactory processDefinitionFactory)
    {
        this.processDefinitionFactory = processDefinitionFactory;
    }


    @Required
    public void setProcessFactory(ProcessFactory processFactory)
    {
        this.processFactory = processFactory;
    }


    @Required
    public void setProcessParameterHelper(ProcessParameterHelper processParameterHelper)
    {
        this.processParameterHelper = processParameterHelper;
    }


    @Required
    public void setBusinessProcessServiceDao(BusinessProcessServiceDao businessProcessServiceDao)
    {
        this.businessProcessServiceDao = businessProcessServiceDao;
    }


    @Required
    public void setTaskService(TaskService taskService)
    {
        this.taskService = taskService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    @Required
    public void setTransactionTemplate(TransactionTemplate transactionTemplate)
    {
        this.transactionTemplate = transactionTemplate;
    }


    public void setBusinessProcessRestartStrategy(BusinessProcessRestartStrategy businessProcessRestartStrategy)
    {
        this.businessProcessRestartStrategy = businessProcessRestartStrategy;
    }


    @Deprecated(since = "ages", forRemoval = true)
    public void triggerEvent(BusinessProcessModel process, String event)
    {
        logDebug("Triggering event: {} in process: {}", new Object[] {event, process});
        boolean lockProcess = this.configurationService.getConfiguration().getBoolean("processengine.event.lockProcess", false);
        if(lockProcess)
        {
            try
            {
                BusinessProcess processItem = (BusinessProcess)getModelService().getSource(process);
                Transaction.current().execute((TransactionBody)new Object(this, processItem, process, event));
            }
            catch(Exception e)
            {
                LOG.error(e.getMessage(), e);
                throw new SystemException(e);
            }
        }
        else
        {
            this.taskService.triggerEvent(process.getCode() + "_" + process.getCode());
        }
    }


    public void triggerEvent(String event)
    {
        BusinessProcessEvent evt = BusinessProcessEvent.newEvent(event);
        triggerEvent(evt);
    }


    public void triggerEvent(String event, Date expirationDate)
    {
        BusinessProcessEvent evt = BusinessProcessEvent.builder(event).withExpirationDate(expirationDate).build();
        triggerEvent(evt);
    }


    public boolean triggerEvent(BusinessProcessEvent event)
    {
        Objects.requireNonNull(event, "event can't be null");
        logDebug("Triggering event: {}", new Object[] {event});
        TaskEvent taskEvent = createTaskEvent(event);
        return this.taskService.triggerEvent(taskEvent);
    }


    private TaskEvent createTaskEvent(BusinessProcessEvent event)
    {
        TaskEvent.Builder resultBuilder = TaskEvent.builder(event.getEvent());
        resultBuilder.withExpirationDate(event.getExpirationDate());
        resultBuilder.withChoice(event.getChoice());
        if(event.isTriggeringInTheFutureEnabled())
        {
            resultBuilder.enableFulfillingNotExistingCondition();
        }
        else
        {
            resultBuilder.disableFulfillingNotExistingCondition();
        }
        return resultBuilder.build();
    }


    private void logDebug(String logString, Object... args)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug(logString, args);
        }
    }
}
