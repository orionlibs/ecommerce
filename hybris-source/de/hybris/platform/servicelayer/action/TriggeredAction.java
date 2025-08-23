package de.hybris.platform.servicelayer.action;

import de.hybris.platform.servicelayer.action.impl.ActionExecutionStrategy;
import de.hybris.platform.servicelayer.model.action.AbstractActionModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;

public class TriggeredAction<T>
{
    private final AbstractActionModel action;
    private final T argument;
    private final ActionExecutionStrategy strategy;


    public TriggeredAction(AbstractActionModel action, T argument, ActionExecutionStrategy strategy)
    {
        ServicesUtil.validateParameterNotNull(action, "Parameter 'action' was null");
        ServicesUtil.validateParameterNotNull(strategy, "Parameter 'strategy' was null");
        this.argument = argument;
        this.action = action;
        this.strategy = strategy;
    }


    public T getArgument()
    {
        return this.argument;
    }


    public AbstractActionModel getAction()
    {
        return this.action;
    }


    public ActionExecutionStrategy getStrategy()
    {
        return this.strategy;
    }
}
