package de.hybris.platform.processengine.actionstrategy;

import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.action.ActionException;
import de.hybris.platform.servicelayer.action.InvalidActionException;
import de.hybris.platform.servicelayer.action.TriggeredAction;
import de.hybris.platform.servicelayer.action.impl.ActionExecutionStrategy;
import de.hybris.platform.servicelayer.enums.ActionType;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.keygenerator.impl.PersistentKeyGenerator;
import de.hybris.platform.servicelayer.model.action.AbstractActionModel;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class ProcessActionExecutionStrategy extends AbstractBusinessService implements ActionExecutionStrategy
{
    private static final Logger LOG = LoggerFactory.getLogger(ProcessActionExecutionStrategy.class);
    public static final String ACTION_PARAM = "triggeredAction";
    public static final String ARGUMENT_PARAM = "triggeredActionArgument";
    private static Set<ActionType> ACCEPTED_TYPES = Collections.singleton(ActionType.PROCESS);
    private BusinessProcessService processService;
    private PersistentKeyGenerator processCodeGenerator;


    public Set<ActionType> getAcceptedTypes()
    {
        return ACCEPTED_TYPES;
    }


    public <T> TriggeredAction<T> prepareAction(AbstractActionModel action, T argument) throws ActionException
    {
        logDebug("Preparing action with code {} and argument {}", new Object[] {action.getCode(), argument});
        Map<String, Object> parameters = new HashMap<>(2);
        parameters.put("triggeredAction", action);
        parameters.put("triggeredActionArgument", argument);
        try
        {
            BusinessProcessModel process = getProcessService().createProcess((String)
                            getProcessCodeGenerator().generate(), action
                            .getTarget(), parameters);
            return (TriggeredAction<T>)new TriggeredProcessAction(action, argument, this, process);
        }
        catch(Exception e)
        {
            throw new ActionException(e);
        }
    }


    public <T> void triggerAction(TriggeredAction<T> preparedAction) throws ActionException
    {
        logDebug("Action {} triggered", new Object[] {preparedAction.getAction()});
        try
        {
            BusinessProcessModel process = ((TriggeredProcessAction)preparedAction).getProcess();
            if(!getModelService().isUpToDate(process))
            {
                getModelService().save(process);
            }
            logDebug("Process: {} started", new Object[] {process});
            getProcessService().startProcess(process);
        }
        catch(Exception e)
        {
            throw new ActionException(e);
        }
    }


    public <T> void cancelAction(TriggeredAction<T> preparedAction) throws ActionException
    {
        BusinessProcessModel process = ((TriggeredProcessAction)preparedAction).getProcess();
        logDebug("Cancelling action: {} of process {}", new Object[] {preparedAction.getAction().getCode(), process});
        if(!getModelService().isNew(process))
        {
            try
            {
                getModelService().remove(process);
            }
            catch(Exception e)
            {
                throw new ActionException("error removing prepared process " + process + " due to " + e.getMessage(), e);
            }
        }
    }


    public void isActionValid(ActionType type, String target) throws InvalidActionException
    {
    }


    @Required
    public void setProcessService(BusinessProcessService processService)
    {
        this.processService = processService;
    }


    public BusinessProcessService getProcessService()
    {
        return this.processService;
    }


    @Required
    public void setProcessCodeGenerator(PersistentKeyGenerator processCodeGenerator)
    {
        this.processCodeGenerator = processCodeGenerator;
    }


    public PersistentKeyGenerator getProcessCodeGenerator()
    {
        return this.processCodeGenerator;
    }


    private void logDebug(String logString, Object... args)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug(logString, args);
        }
    }
}
