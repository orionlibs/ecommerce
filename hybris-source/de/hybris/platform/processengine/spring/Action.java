package de.hybris.platform.processengine.spring;

import de.hybris.platform.processengine.definition.ActionDefinitionContext;
import de.hybris.platform.processengine.definition.ActionDefinitionContextHolder;
import de.hybris.platform.task.RetryLaterException;
import java.util.Set;

public interface Action<T extends de.hybris.platform.processengine.model.BusinessProcessModel>
{
    public static final String RETRY_RETURN_CODE = "retry";
    public static final String ERROR_RETURN_CODE = "error";


    String execute(T paramT) throws RetryLaterException, Exception;


    default ActionDefinitionContext getCurrentActionDefinitionContext()
    {
        return ActionDefinitionContextHolder.getActionDefinitionContext();
    }


    Set<String> getTransitions();
}
