package de.hybris.platform.servicelayer.action.impl;

import de.hybris.platform.servicelayer.action.ActionException;
import de.hybris.platform.servicelayer.action.ActionService;
import de.hybris.platform.servicelayer.action.InvalidActionException;
import de.hybris.platform.servicelayer.action.TriggeredAction;
import de.hybris.platform.servicelayer.enums.ActionType;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.model.action.AbstractActionModel;
import org.springframework.beans.factory.annotation.Required;

public class DefaultActionService extends AbstractBusinessService implements ActionService
{
    private ActionExecutionStrategyRegistry executionStrategyRegistry;


    public void isActionValid(ActionType type, String target) throws InvalidActionException
    {
        getExecutionStrategyRegistry().getExecutionStrategy(type).isActionValid(null, target);
    }


    public <T> TriggeredAction<T> prepareAction(AbstractActionModel action, T argument) throws ActionException
    {
        return getExecutionStrategyRegistry().getExecutionStrategy(action.getType()).prepareAction(action, argument);
    }


    public <T> void triggerAction(TriggeredAction<T> preparedAction) throws ActionException
    {
        getExecutionStrategyRegistry().getExecutionStrategy(preparedAction.getAction().getType()).triggerAction(preparedAction);
    }


    public <T> void cancelAction(TriggeredAction<T> preparedAction) throws ActionException
    {
        getExecutionStrategyRegistry().getExecutionStrategy(preparedAction.getAction().getType()).cancelAction(preparedAction);
    }


    public <T> TriggeredAction<T> prepareAndTriggerAction(AbstractActionModel action, T argument) throws ActionException
    {
        TriggeredAction<T> ret = prepareAction(action, argument);
        triggerAction(ret);
        return ret;
    }


    @Required
    public void setExecutionStrategyRegistry(ActionExecutionStrategyRegistry executionStrategyRegistry)
    {
        this.executionStrategyRegistry = executionStrategyRegistry;
    }


    public ActionExecutionStrategyRegistry getExecutionStrategyRegistry()
    {
        return this.executionStrategyRegistry;
    }
}
