package de.hybris.platform.processengine.action;

import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.task.RetryLaterException;
import java.util.Set;

public abstract class AbstractProceduralAction<T extends BusinessProcessModel> extends AbstractAction<T>
{
    public Set<String> getTransitions()
    {
        return Transition.getStringValues();
    }


    public final String execute(T process) throws RetryLaterException, Exception
    {
        executeAction(process);
        return Transition.OK.toString();
    }


    public abstract void executeAction(T paramT) throws RetryLaterException, Exception;
}
