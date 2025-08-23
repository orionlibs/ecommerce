package de.hybris.platform.servicelayer.action.plain;

import de.hybris.platform.servicelayer.action.TriggeredAction;
import de.hybris.platform.servicelayer.action.impl.ActionExecutionStrategy;
import de.hybris.platform.servicelayer.action.impl.ActionPerformable;
import de.hybris.platform.servicelayer.model.action.AbstractActionModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;

public class TriggeredPlainAction<T> extends TriggeredAction<T>
{
    private final ActionPerformable<T> performable;


    public TriggeredPlainAction(AbstractActionModel action, T argument, ActionExecutionStrategy strategy, ActionPerformable<T> performable)
    {
        super(action, argument, strategy);
        ServicesUtil.validateParameterNotNull(performable, "Parameter 'performable' was null");
        this.performable = performable;
    }


    public ActionPerformable<T> getPerformable()
    {
        return this.performable;
    }
}
