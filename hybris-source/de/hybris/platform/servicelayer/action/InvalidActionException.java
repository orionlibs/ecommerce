package de.hybris.platform.servicelayer.action;

import de.hybris.platform.servicelayer.enums.ActionType;
import de.hybris.platform.servicelayer.model.action.AbstractActionModel;

public class InvalidActionException extends ActionException
{
    private final ActionType type;
    private final String target;
    private final AbstractActionModel action;


    public InvalidActionException(String message, Throwable cause, ActionType type, String target)
    {
        super(message, cause);
        this.type = type;
        this.target = target;
        this.action = null;
    }


    public InvalidActionException(String message, Throwable cause, AbstractActionModel action)
    {
        super(message, cause);
        this.action = action;
        this.target = action.getTarget();
        this.type = action.getType();
    }


    public ActionType getType()
    {
        return this.type;
    }


    public String getTarget()
    {
        return this.target;
    }


    public AbstractActionModel getAction()
    {
        return this.action;
    }
}
