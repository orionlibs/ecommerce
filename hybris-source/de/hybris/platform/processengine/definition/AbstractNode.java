package de.hybris.platform.processengine.definition;

import de.hybris.platform.core.Registry;
import de.hybris.platform.processengine.helpers.ProcessFactory;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.model.ProcessTaskModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.TaskService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class AbstractNode implements Node
{
    private final String nodeId;


    AbstractNode(String nodeId)
    {
        this.nodeId = nodeId;
    }


    public String getId()
    {
        return this.nodeId;
    }


    public String execute(BusinessProcessModel process) throws NodeExecutionException
    {
        logDebug("Executing process: {} on node: {}", new Object[] {process, this.nodeId});
        trigger(process);
        return "OK";
    }


    protected TaskService getTaskManager()
    {
        return (TaskService)Registry.getApplicationContext().getBean("taskService");
    }


    protected ModelService getModelService()
    {
        return (ModelService)Registry.getApplicationContext().getBean("modelService");
    }


    protected ProcessDefinitionFactory getProcessDefinitionFactory()
    {
        return (ProcessDefinitionFactory)Registry.getApplicationContext().getBean("processDefinitionFactory");
    }


    protected ProcessFactory getProcessHelper()
    {
        return (ProcessFactory)Registry.getApplicationContext().getBean("processHelper");
    }


    @Deprecated(since = "ages", forRemoval = true)
    protected ProcessDefinition getProcessDefinition(String processDefinitionName)
    {
        return getProcessDefinition(new ProcessDefinitionId(processDefinitionName));
    }


    protected ProcessDefinition getProcessDefinition(ProcessDefinitionId id)
    {
        return getProcessDefinitionFactory().getProcessDefinition(id);
    }


    protected void bindTaskToProcessDefaultNodeGroup(ProcessTaskModel task, BusinessProcessModel process)
    {
        ProcessDefinition procDef = getProcessDefinition(ProcessDefinitionId.of(process));
        String defaultNodeGroup = procDef.getDefaultNodeGroup();
        if(StringUtils.isNotBlank(defaultNodeGroup))
        {
            task.setNodeGroup(defaultNodeGroup);
            logDebug("binding action {} to cluster node group {} (process default)", new Object[] {this, defaultNodeGroup});
        }
    }


    protected void triggerNextCall(BusinessProcessModel process, ProcessTaskModel taskModel, Node node)
    {
        boolean runInSynchronusMode = false;
        if(canTriggerNextCallSynchronously() && node instanceof SynchronusAwareNode)
        {
            SynchronusAwareNode synchNode = (SynchronusAwareNode)node;
            runInSynchronusMode = (synchNode.canBeTriggeredForTask(taskModel) && !loopDetected(node));
            if(runInSynchronusMode)
            {
                logDebug("Running in synchronous mode process: {}, taskModel: {}, on node: {}", new Object[] {process, taskModel, node.getId()});
                synchNode.triggerForTask(taskModel);
            }
        }
        if(!runInSynchronusMode)
        {
            logDebug("Running process: {} on node: {}", new Object[] {process, node});
            node.trigger(process);
        }
    }


    private boolean loopDetected(Node nodeToCheck)
    {
        return getId().equals(nodeToCheck.getId());
    }


    protected boolean canTriggerNextCallSynchronously()
    {
        return true;
    }


    protected void logDebug(String logString, Object... args)
    {
        Logger logger = LoggerFactory.getLogger(AbstractNode.class);
        if(logger.isDebugEnabled())
        {
            logger.debug(logString, args);
        }
    }
}
