package de.hybris.platform.processengine.action;

import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.task.RetryLaterException;
import java.util.Set;

public abstract class AbstractSimpleDecisionAction<T extends BusinessProcessModel> extends AbstractAction<T>
{
    public Set<String> getTransitions()
    {
        return Transition.getStringValues();
    }


    public final String execute(T process) throws RetryLaterException, Exception
    {
        return executeAction(process).toString();
    }


    public abstract Transition executeAction(T paramT) throws RetryLaterException, Exception;
}
