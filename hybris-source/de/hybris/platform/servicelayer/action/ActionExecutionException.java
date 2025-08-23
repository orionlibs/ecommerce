package de.hybris.platform.servicelayer.action;

public class ActionExecutionException extends ActionException
{
    private final TriggeredAction triggeredAction;


    public ActionExecutionException(String message, Throwable t, TriggeredAction triggeredAction)
    {
        super(message, t);
        this.triggeredAction = triggeredAction;
    }


    public TriggeredAction getTriggeredAction()
    {
        return this.triggeredAction;
    }
}
