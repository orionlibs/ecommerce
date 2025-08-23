package de.hybris.platform.processengine.definition;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import de.hybris.platform.core.Registry;
import de.hybris.platform.processengine.jalo.BusinessProcess;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.processengine.model.BusinessProcessParameterModel;
import de.hybris.platform.processengine.model.ProcessTaskModel;
import de.hybris.platform.scripting.engine.ScriptExecutionResult;
import de.hybris.platform.scripting.engine.ScriptingLanguagesService;
import de.hybris.platform.scripting.engine.content.ScriptContent;
import de.hybris.platform.scripting.engine.content.impl.SimpleScriptContent;
import de.hybris.platform.scripting.enums.ScriptType;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.task.TaskConditionModel;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

class WaitNode extends AbstractNode implements SupportsTimeout
{
    private static final Logger LOG = Logger.getLogger(WaitNode.class);
    private final String event;
    private final String then;
    private final boolean prependProcessCode;
    private final Map<String, String> choices;
    private final TimeoutConfiguration timeoutConfiguration;
    private final ScriptingLanguagesService scriptingLanguagesService;
    private static final String TIMEOUT_MSG = "Timeout is not configured";
    private static final String WAIT_NODE_ERR_MSG = "Wait node %s for process with code %s has been triggered with multiple choices: %s which is not supported";


    WaitNode(String id, String event, String then, boolean prependProcessCode, Map<String, String> choices, TimeoutConfiguration timeoutConfiguration)
    {
        super(id);
        Preconditions.checkArgument(!Strings.isNullOrEmpty(event), "Event to wait for must not be null or empty.");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(then), "Successor node of '" + id + "' must not be null or empty.");
        Preconditions.checkNotNull(choices, "choices can't be null");
        Preconditions.checkNotNull(timeoutConfiguration, "timeoutConfiguration can't be null");
        this.prependProcessCode = prependProcessCode;
        this.event = event;
        this.then = then;
        this.choices = (Map<String, String>)ImmutableMap.copyOf(choices);
        this.timeoutConfiguration = timeoutConfiguration;
        this.scriptingLanguagesService = (ScriptingLanguagesService)Registry.getApplicationContext().getBean("scriptingLanguagesService", ScriptingLanguagesService.class);
    }


    public boolean isExecutionContextRequired()
    {
        return true;
    }


    public void trigger(BusinessProcessModel process)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug(String.format("Scheduling wait for event [%s] in node with ID [%s] for process with code [%s]", new Object[] {getEvent(),
                            getId(), process.getCode()}));
        }
        ProcessTaskModel task = new ProcessTaskModel();
        task.setRunnerBean("taskRunner");
        task.setAction(getId());
        task.setProcess(process);
        bindTaskToProcessDefaultNodeGroup(task, process);
        TaskConditionModel taskCondition = (TaskConditionModel)getModelService().create(TaskConditionModel.class);
        String code = replaceWildCardFromContext(this.event, process, process.getContextParameters());
        if(this.prependProcessCode)
        {
            code = process.getCode() + "_" + process.getCode();
        }
        taskCondition.setUniqueID(code);
        task.setConditions(Collections.singleton(taskCondition));
        if(this.timeoutConfiguration.isTimeoutConfigured())
        {
            task.setExpirationTimeMillis(this.timeoutConfiguration.getExpirationTime());
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug(String.format("Scheduling wait for event [%s] in node with ID [%s] for process with code [%s]. Unique ID of condition is [%s]", new Object[] {getEvent(), getId(), process.getCode(), taskCondition.getUniqueID()}));
        }
        ConfigurationService configurationService = (ConfigurationService)Registry.getApplicationContext().getBean("configurationService");
        boolean lockProcess = configurationService.getConfiguration().getBoolean("processengine.event.lockProcess", false);
        if(lockProcess)
        {
            BusinessProcess processItem = (BusinessProcess)getModelService().getSource(process);
            String theCode = code;
            try
            {
                Transaction.current().execute((TransactionBody)new Object(this, processItem, process, task, theCode));
            }
            catch(Exception e)
            {
                LOG.error(e.getMessage(), e);
                throw new SystemException(e);
            }
        }
        else
        {
            scheduleTask(process, task, code);
        }
    }


    private void scheduleTask(BusinessProcessModel process, ProcessTaskModel task, String code)
    {
        try
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(String.format("Scheduling wait for event %s, in node with ID %s for process with code %s. Task condition unique ID %s", new Object[] {getEvent(), getId(), process.getCode(), code}));
            }
            getTaskManager().scheduleTask((TaskModel)task);
        }
        catch(ModelSavingException e)
        {
            LOG.error(String.format("Error scheduling wait for event [%s] in node with ID [%s] for process with code [%s]. Task condition has unique ID [%s].", new Object[] {getEvent(), getId(), process.getCode(), code}));
            throw e;
        }
    }


    private String replaceWildCardFromContext(String input, BusinessProcessModel process, Collection<BusinessProcessParameterModel> values)
    {
        Map<String, Object> valuesToPass = new HashMap<>();
        if(values != null)
        {
            for(BusinessProcessParameterModel param : values)
            {
                valuesToPass.put(param.getName(), param.getValue());
            }
        }
        return replaceWildCardFromContext(input, process, valuesToPass);
    }


    private String replaceWildCardFromContext(String input, BusinessProcessModel process, Map<String, Object> values)
    {
        String content = "\"" + input + "\"";
        SimpleScriptContent scriptContent = new SimpleScriptContent(ScriptType.GROOVY.getCode(), content);
        ImmutableMap immutableMap = ImmutableMap.builder().put("process", process).put("params", values).build();
        ScriptExecutionResult result = this.scriptingLanguagesService.getExecutableByContent((ScriptContent)scriptContent).execute((Map)immutableMap);
        return (result == null || result.getScriptResult() == null) ? input : result.getScriptResult().toString();
    }


    public String execute(BusinessProcessModel process) throws NodeExecutionException
    {
        throw new UnsupportedOperationException("WaitNode requires context to be executed");
    }


    public String executeWithContext(NodeExecutionContext executionContext) throws NodeExecutionException
    {
        BusinessProcessModel process = executionContext.getBusinessProcessModel();
        String nextNodeId = getNextNodeBasedOnContext(executionContext);
        if(LOG.isDebugEnabled())
        {
            LOG.debug(String.format("Event %s received in node with ID %s for process with code %s. Event provided following choices %s. Next is node with ID %s", new Object[] {getEvent(), getId(), process.getCode(), executionContext.getChoices(), nextNodeId}));
        }
        ProcessDefinition processDef = getProcessDefinition(ProcessDefinitionId.of(process));
        Node next = processDef.retrieve(nextNodeId);
        triggerNextCall(process, executionContext.getProcessTaskModel(), next);
        return "OK";
    }


    protected boolean canTriggerNextCallSynchronously()
    {
        return false;
    }


    public String handleTimeout(NodeExecutionContext executionContext) throws NodeExecutionException
    {
        BusinessProcessModel process = executionContext.getBusinessProcessModel();
        String nextNodeId = getTimeoutNode(executionContext);
        if(LOG.isDebugEnabled())
        {
            LOG.debug(
                            String.format("Timeout occured in node with ID %s for process with code %s. Next is node with ID %s", new Object[] {getId(), process
                                            .getCode(), nextNodeId}));
        }
        ProcessDefinition processDef = getProcessDefinition(ProcessDefinitionId.of(process));
        Node next = processDef.retrieve(nextNodeId);
        next.trigger(process);
        return "OK";
    }


    public final String getEvent()
    {
        return this.event;
    }


    public final String getThen()
    {
        return this.then;
    }


    private String getNextNodeBasedOnContext(NodeExecutionContext context)
    {
        if(!context.hasAnyChoice())
        {
            return this.then;
        }
        String userChoice = getUserChoiceFromContext(context);
        String nextNode = this.choices.get(userChoice);
        if(nextNode == null)
        {
            String msg = "Wait node " + getId() + " for process with code " + context.getBusinessProcessModel().getCode() + " has been triggered with choice " + userChoice + " which hadn't been defined in process definition";
            LOG.error(msg);
            throw new IllegalStateException(msg);
        }
        return nextNode;
    }


    private String getTimeoutNode(NodeExecutionContext context)
    {
        if(!this.timeoutConfiguration.isTimeoutConfigured())
        {
            String msg = "Wait node " + getId() + " for process with code " + context.getBusinessProcessModel().getCode() + " has timed out but timeout is not configured";
            LOG.error(msg);
            throw new IllegalStateException(msg);
        }
        return this.timeoutConfiguration.getTimeoutAction();
    }


    private String getUserChoiceFromContext(NodeExecutionContext context)
    {
        Preconditions.checkArgument(context.hasAnyChoice(), "Context should have at least one choice.");
        Set<String> userChoices = context.getChoices();
        if(userChoices.size() > 1)
        {
            String errorMsg = String.format("Wait node %s for process with code %s has been triggered with multiple choices: %s which is not supported", new Object[] {getId(), context.getBusinessProcessModel().getCode(), context
                            .getChoices()});
            LOG.error(errorMsg);
            throw new IllegalStateException(errorMsg);
        }
        return (String)Iterables.getOnlyElement(userChoices);
    }
}
