package de.hybris.platform.workflow.jalo;

import de.hybris.platform.jalo.JaloSystemException;

@Deprecated(since = "ages", forRemoval = false)
public class WorkflowActionDecideException extends JaloSystemException
{
    public WorkflowActionDecideException(Throwable nested)
    {
        super(nested.getMessage());
    }


    public WorkflowActionDecideException(String message)
    {
        super(message);
    }


    public WorkflowActionDecideException(Throwable nested, String message, int errorCode)
    {
        super(nested, message, errorCode);
    }
}
