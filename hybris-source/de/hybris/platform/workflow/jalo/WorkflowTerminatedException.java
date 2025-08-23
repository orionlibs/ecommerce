package de.hybris.platform.workflow.jalo;

import de.hybris.platform.jalo.JaloSystemException;

@Deprecated(since = "ages", forRemoval = false)
public class WorkflowTerminatedException extends JaloSystemException
{
    public WorkflowTerminatedException(Throwable nested)
    {
        super(nested.getMessage());
    }


    public WorkflowTerminatedException(String message)
    {
        super(message);
    }


    public WorkflowTerminatedException(Throwable nested, String message, int errorCode)
    {
        super(nested, message, errorCode);
    }
}
