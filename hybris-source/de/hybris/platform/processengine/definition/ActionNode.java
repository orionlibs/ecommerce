package de.hybris.platform.processengine.definition;

import de.hybris.platform.core.Registry;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.model.ProcessTaskModel;
import de.hybris.platform.processengine.spring.Action;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.util.Utilities;
import java.sql.SQLTransactionRollbackException;
import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.ConcurrencyFailureException;

class ActionNode extends AbstractNode implements SynchronusAwareNode
{
    private static final Logger LOG = LoggerFactory.getLogger(ActionNode.class);
    private final ActionDefinitionContext actionDefinitionContext;
    private final Integer clusterNodeId;
    private final String clusterGroup;
    private final Action bean;
    private final boolean canJoinPreviousNode;


    ActionNode(String nodeId, Integer clusterNodeId, String clusterGroup, String actionBeanName, ActionDefinitionContext actionDefinitionContext, boolean canJoinPreviousNode)
    {
        this(nodeId, clusterNodeId, clusterGroup, (Action)Registry.getApplicationContext().getBean(actionBeanName), actionDefinitionContext, canJoinPreviousNode);
    }


    ActionNode(String nodeId, Integer clusterNodeId, String clusterGroup, Action action, ActionDefinitionContext actionDefinitionContext, boolean canJoinPreviousNode)
    {
        super(nodeId);
        this.bean = action;
        this.actionDefinitionContext = actionDefinitionContext;
        this.clusterNodeId = clusterNodeId;
        this.clusterGroup = clusterGroup;
        this.canJoinPreviousNode = canJoinPreviousNode;
        if(StringUtils.isNotBlank(clusterGroup) && clusterNodeId != null)
        {
            LOG.warn("Got action with ambiguous settings: clusterNodeID={} *and* clusterGroup='{}' -> will be ran on clusterNode {} only since this setting is more strict!", new Object[] {clusterNodeId, clusterGroup, clusterNodeId});
        }
        Set<String> tmp = this.bean.getTransitions();
        for(String returnCode : tmp)
        {
            if("retry".equals(returnCode) || "error".equals(returnCode))
            {
                continue;
            }
            transition(returnCode);
        }
    }


    private String transition(String returnCode)
    {
        String ret = (String)this.actionDefinitionContext.getTransitions().get(returnCode);
        if(ret == null)
        {
            throw new InvalidProcessDefinitionException("Possible return code '" + returnCode + "' of bean '" + this.bean
                            .getClass()
                            .getName() + "' is not mapped in transitions fo action '" +
                            getId() + "'.");
        }
        return ret;
    }


    public String getTransistion(String returnCode)
    {
        return transition(returnCode);
    }


    public Optional<String> getParameter(String parameterName)
    {
        return this.actionDefinitionContext.getParameter(parameterName);
    }


    public void trigger(BusinessProcessModel process)
    {
        ProcessTaskModel task = new ProcessTaskModel();
        task.setAction(getId());
        task.setExecutionDate(new Date());
        task.setProcess(process);
        bindClusterNodeOrGroup(task, process);
        task.setRunnerBean("taskRunner");
        logDebug("Scheduling task with action ID: {} for process with code: {} to be executed at: {}", new Object[] {getId(), process.getCode(), task.getExecutionDate()});
        getTaskManager().scheduleTask((TaskModel)task);
    }


    protected void bindClusterNodeOrGroup(ProcessTaskModel task, BusinessProcessModel process)
    {
        task.setNodeId(this.clusterNodeId);
        if(StringUtils.isNotBlank(this.clusterGroup))
        {
            task.setNodeGroup(this.clusterGroup);
            logDebug("binding action {} to cluster node group {}", new Object[] {this, this.clusterNodeId});
        }
        else
        {
            bindTaskToProcessDefaultNodeGroup(task, process);
        }
    }


    public String execute(BusinessProcessModel process) throws NodeExecutionException
    {
        throw new UnsupportedOperationException("ActionNode requires context to be executed");
    }


    private void handleActionExecutionRuntimeException(RuntimeException e) throws NodeExecutionException
    {
        if(isCausedByRollbackException(e))
        {
            LOG.warn("Transaction rollback exception has occured. Action has to be retried.");
            logDebug("Exception: ", new Object[] {e});
            RetryLaterException retryException = new RetryLaterException(e.getMessage(), e);
            retryException.setDelay((long)(1000.0D * (new Random()).nextDouble()) + 100L);
            throw retryException;
        }
        LOG.error("Error executing", e);
        throw new NodeExecutionException("Error executing ActionNode with ID [" + getId() + "]: " + e.getMessage(), e);
    }


    private boolean isCausedByRollbackException(RuntimeException e)
    {
        return (Utilities.getRootCauseOfType(e, SQLTransactionRollbackException.class) != null ||
                        Utilities.getRootCauseOfType(e, ConcurrencyFailureException.class) != null);
    }


    private void afterExecution(BusinessProcessModel process, String returnCode, ProcessTaskModel taskModel) throws NodeExecutionException
    {
        if("retry".equals(returnCode))
        {
            throw new RetryLaterException();
        }
        try
        {
            if("error".equals(returnCode))
            {
                throw new NodeExecutionException("Action with ID [" +
                                getId() + "] returned error code: error");
            }
            String nodeId = transition(returnCode);
            Node node = getProcessDefinition(ProcessDefinitionId.of(process)).retrieve(nodeId);
            triggerNextCall(process, taskModel, node);
        }
        catch(InvalidProcessDefinitionException e)
        {
            throw new NodeExecutionException("Unable to trigger next node: " + e.getMessage(), e);
        }
    }


    public String executeWithContext(NodeExecutionContext executionContext) throws NodeExecutionException
    {
        BusinessProcessModel process = executionContext.getBusinessProcessModel();
        logDebug("Executing action with ID {} for process with code {}", new Object[] {getId(), process.getCode()});
        String result = null;
        ActionDefinitionContextHolder.setActionDefinitionContext(this.actionDefinitionContext);
        try
        {
            result = this.bean.execute(process);
            logDebug("Finished execution of: {}, result: {}", new Object[] {process, result});
            afterExecution(process, result, executionContext.getProcessTaskModel());
        }
        catch(RetryLaterException | NodeExecutionException e)
        {
            throw e;
        }
        catch(RuntimeException e)
        {
            handleActionExecutionRuntimeException(e);
        }
        catch(Exception e)
        {
            LOG.error("Error executing", e);
            throw new NodeExecutionException("Error executing ActionNode with ID [" + getId() + "]: " + e.getMessage(), e);
        }
        finally
        {
            ActionDefinitionContextHolder.unsetActionDefinitionContext();
        }
        return result;
    }


    public boolean isExecutionContextRequired()
    {
        return true;
    }


    public boolean canBeTriggeredForTask(ProcessTaskModel processTaskModel)
    {
        return isCanJoinPreviousNode();
    }


    public void triggerForTask(ProcessTaskModel task)
    {
        logDebug("Triggering action: {} for task: {}", new Object[] {getId(), task});
        task.setAction(getId());
        getModelService().save(task);
    }


    public boolean isCanJoinPreviousNode()
    {
        return this.canJoinPreviousNode;
    }
}
