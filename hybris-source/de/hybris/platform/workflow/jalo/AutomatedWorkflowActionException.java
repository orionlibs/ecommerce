package de.hybris.platform.workflow.jalo;

import de.hybris.platform.jalo.JaloSystemException;

@Deprecated(since = "ages", forRemoval = false)
public class AutomatedWorkflowActionException extends JaloSystemException
{
    public AutomatedWorkflowActionException(Throwable nested)
    {
        super(nested.getMessage());
    }


    public AutomatedWorkflowActionException(String message)
    {
        super(message);
    }


    public AutomatedWorkflowActionException(Throwable nested, String message, int errorCode)
    {
        super(nested, message, errorCode);
    }
}
