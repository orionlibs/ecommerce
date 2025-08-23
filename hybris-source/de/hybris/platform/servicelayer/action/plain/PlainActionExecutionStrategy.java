package de.hybris.platform.servicelayer.action.plain;

import de.hybris.platform.servicelayer.action.ActionException;
import de.hybris.platform.servicelayer.action.ActionExecutionException;
import de.hybris.platform.servicelayer.action.InvalidActionException;
import de.hybris.platform.servicelayer.action.TriggeredAction;
import de.hybris.platform.servicelayer.action.impl.ActionExecutionStrategy;
import de.hybris.platform.servicelayer.action.impl.ActionPerformable;
import de.hybris.platform.servicelayer.enums.ActionType;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.model.action.AbstractActionModel;
import java.util.Collections;
import java.util.Set;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class PlainActionExecutionStrategy extends AbstractBusinessService implements ActionExecutionStrategy, ApplicationContextAware
{
    private static final Set<ActionType> ACCEPTED = Collections.singleton(ActionType.PLAIN);
    private ApplicationContext applicationContext;


    public Set<ActionType> getAcceptedTypes()
    {
        return ACCEPTED;
    }


    public <T> TriggeredAction<T> prepareAction(AbstractActionModel action, T argument) throws ActionException
    {
        ActionPerformable<T> perf = getPerformable(action.getTarget());
        return (TriggeredAction<T>)new TriggeredPlainAction(action, argument, this, perf);
    }


    public <T> void triggerAction(TriggeredAction<T> preparedAction) throws ActionException
    {
        ActionPerformable<T> perf = ((TriggeredPlainAction)preparedAction).getPerformable();
        try
        {
            perf.performAction(preparedAction.getAction(), preparedAction.getArgument());
        }
        catch(Throwable t)
        {
            throw new ActionExecutionException("error performing " + preparedAction.getAction() + " using " + perf + " : " + t
                            .getMessage(), t, preparedAction);
        }
    }


    public <T> void cancelAction(TriggeredAction<T> preparedAction) throws ActionException
    {
    }


    protected ActionPerformable getPerformable(String target)
    {
        try
        {
            return (ActionPerformable)getApplicationContext().getBean(target, ActionPerformable.class);
        }
        catch(BeansException e)
        {
            throw new InvalidActionException("error gett performable for target '" + target + "'", e, ActionType.PLAIN, target);
        }
    }


    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext = applicationContext;
    }


    public ApplicationContext getApplicationContext()
    {
        return this.applicationContext;
    }


    public void isActionValid(ActionType type, String target) throws InvalidActionException
    {
        getPerformable(target);
    }
}
