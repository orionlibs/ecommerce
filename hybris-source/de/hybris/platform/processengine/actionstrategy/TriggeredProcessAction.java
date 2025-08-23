package de.hybris.platform.processengine.actionstrategy;

import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.action.TriggeredAction;
import de.hybris.platform.servicelayer.action.impl.ActionExecutionStrategy;
import de.hybris.platform.servicelayer.model.action.AbstractActionModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;

public class TriggeredProcessAction<T> extends TriggeredAction<T>
{
    private final BusinessProcessModel process;


    public TriggeredProcessAction(AbstractActionModel action, T argument, ActionExecutionStrategy strategy, BusinessProcessModel process)
    {
        super(action, argument, strategy);
        ServicesUtil.validateParameterNotNull(process, "Parameter 'process' was null");
        this.process = process;
    }


    public BusinessProcessModel getProcess()
    {
        return this.process;
    }
}
